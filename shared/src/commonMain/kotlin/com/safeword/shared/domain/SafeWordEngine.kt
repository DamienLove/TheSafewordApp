package com.safeword.shared.domain

import com.safeword.shared.bridge.PeerBridge
import com.safeword.shared.bridge.model.PeerBridgeEvent
import com.safeword.shared.bridge.model.PeerBridgeState
import com.safeword.shared.domain.model.AlertEvent
import com.safeword.shared.domain.model.AlertSource
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.model.ContactEngagementType
import com.safeword.shared.domain.model.ContactLinkStatus
import com.safeword.shared.domain.model.AlertProfile
import com.safeword.shared.domain.model.PlanTier
import com.safeword.shared.domain.model.SafeWordSettings
import com.safeword.shared.domain.model.AlertSound
import com.safeword.shared.domain.repository.AlertEventRepository
import com.safeword.shared.domain.repository.ContactRepository
import com.safeword.shared.domain.repository.SettingsGateway
import com.safeword.shared.domain.service.EmergencyDispatcher
import com.safeword.shared.config.Defaults
import com.safeword.shared.util.TimeProvider
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random

class SafeWordEngine(
    private val settingsGateway: SettingsGateway,
    private val contactRepository: ContactRepository,
    private val alertEventRepository: AlertEventRepository,
    private val dispatcher: EmergencyDispatcher,
    private val peerBridge: PeerBridge,
    private val timeProvider: TimeProvider,
    private val scope: CoroutineScope,
    private val localPlanTier: PlanTier
) {

    private val engineScope = CoroutineScope(scope.coroutineContext + Job())
    private val pendingSignals = mutableMapOf<String, CompletableDeferred<Boolean>>()
    private val pendingMutex = Mutex()

    val dashboardState: StateFlow<DashboardState> =
        combine(settingsGateway.settings, contactRepository.observeContacts(), peerBridge.state) { settings, contacts, bridgeState ->
            DashboardState(
                settings = settings,
                contacts = contacts,
                bridgeState = bridgeState
            )
        }
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = engineScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = DashboardState()
            )

    init {
        engineScope.launch {
            peerBridge.startAdvertising()
        }
        engineScope.launch {
            peerBridge.incomingEvents.collect { event ->
                when (event) {
                    is PeerBridgeEvent.AlertBroadcast -> handleIncomingPeerAlert(event.payload)
                    is PeerBridgeEvent.CheckIn -> handleIncomingCheckIn(event)
                    else -> Unit
                }
            }
        }
    }

    suspend fun processTranscript(transcript: String) {
        val settings = dashboardState.value.settings ?: return
        if (!settings.listeningEnabled || settings.safeWords().isEmpty()) return
        val normalised = transcript.lowercase()
        val primary = settings.safeWordOne.lowercase().takeIf { it.isNotBlank() }
        val secondary = settings.safeWordTwo.lowercase().takeIf { it.isNotBlank() }
        when {
            primary != null && normalised.contains(primary) ->
                triggerEmergency(settings.safeWordOne, AlertSource.VOICE, emergency = true)
            secondary != null && normalised.contains(secondary) ->
                triggerEmergency(settings.safeWordTwo, AlertSource.VOICE, emergency = false)
        }
    }

    suspend fun processSms(body: String, senderPhone: String?) {
        val settings = dashboardState.value.settings ?: return
        if (body.contains(CONTACT_SIGNAL_PREFIX)) {
            handleIncomingContactSignal(senderPhone, body)
            return
        }
        if (settings.safeWords().isEmpty()) return
        val normalised = body.lowercase()
        val primary = settings.safeWordOne.lowercase().takeIf { it.isNotBlank() }
        val secondary = settings.safeWordTwo.lowercase().takeIf { it.isNotBlank() }
        when {
            primary != null && normalised.contains(primary) ->
                triggerEmergency(settings.safeWordOne, AlertSource.SMS, broadcast = true, emergency = true)
            secondary != null && normalised.contains(secondary) ->
                triggerEmergency(settings.safeWordTwo, AlertSource.SMS, broadcast = false, emergency = false)
        }
    }

    suspend fun runTest() {
        triggerEmergency(detectedWord = "TEST", source = AlertSource.TEST, broadcast = false)
    }

    suspend fun triggerManual(
        word: String,
        source: AlertSource,
        broadcast: Boolean = true,
        emergency: Boolean = true
    ) {
        triggerEmergency(detectedWord = word, source = source, broadcast = broadcast, emergency = emergency)
    }

    suspend fun sendCheckIn(contact: Contact): Boolean {
        val state = dashboardState.value.bridgeState
        if (state !is PeerBridgeState.Connected || state.peerCount <= 0) {
            return false
        }
        val timestamp = timeProvider.nowMillis()
        val event = PeerBridgeEvent.CheckIn(
            contactName = contact.name,
            contactPhone = contact.phone,
            message = "Check in request for ${contact.name}",
            timestampMillis = timestamp
        )
        peerBridge.broadcast(event)
        return true
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun sendContactSignal(
        contact: Contact,
        type: ContactEngagementType,
        emergency: Boolean,
        message: String? = null
    ): Boolean {
        if (type != ContactEngagementType.INVITE && contact.linkStatus == ContactLinkStatus.UNLINKED) return false
        val settings = dashboardState.value.settings ?: return false
        val timestamp = timeProvider.nowMillis()
        val includeLocation = settings.includeLocation && type != ContactEngagementType.INVITE
        val location = if (includeLocation) dispatcher.resolveLocation() else null
        val sessionId = "${timestamp.toString(36)}-${Random.nextInt(1000, 9999)}"
        val parts = mutableListOf(
            CONTACT_SIGNAL_HEADER,
            "$KEY_TYPE=${type.name}",
            "$KEY_EMERGENCY=${if (emergency) 1 else 0}",
            "$KEY_TIMESTAMP=$timestamp",
            "$KEY_PLAN=${localPlanTier.name}",
            "$KEY_STATE=$STATE_REQUEST",
            "$KEY_SESSION=$sessionId"
        )
        location?.let { (lat, lon) ->
            parts += "$KEY_LAT=$lat"
            parts += "$KEY_LON=$lon"
        }
        val cleanedMessage = message?.trim()?.takeIf { it.isNotEmpty() }
        val encodedMessage = cleanedMessage?.let { Base64.Default.encode(it.encodeToByteArray()) }
        if (encodedMessage != null) {
            parts += "$KEY_MESSAGE=$encodedMessage"
        }
        val payload = parts.joinToString(separator = "|")
        val locationLink = location?.let { " https://maps.google.com/?q=${it.first},${it.second}" } ?: ""
        val friendly = when (type) {
            ContactEngagementType.CALL -> {
                val tone = if (emergency) "Emergency" else "Check-in"
                "$tone call request via SafeWord.$locationLink"
            }
            ContactEngagementType.TEXT -> {
                val tone = if (emergency) "Emergency" else "Check-in"
                buildString {
                    append("$tone message via SafeWord.$locationLink")
                    if (cleanedMessage != null) {
                        append("\nMessage: ")
                        append(cleanedMessage)
                    }
                }
            }
            ContactEngagementType.INVITE -> buildInviteMessage()
        }
        val body = buildString {
            append(CONTACT_SIGNAL_PREFIX)
            append(' ')
            append(payload)
            append('\n')
            append(friendly)
        }
        val expectAck = type != ContactEngagementType.INVITE
        val deferred = if (expectAck) CompletableDeferred<Boolean>() else null
        if (deferred != null) {
            pendingMutex.withLock { pendingSignals[sessionId] = deferred }
        }
        val sent = dispatcher.sendSms(body, listOf(contact)) > 0
        if (!sent) {
            if (deferred != null) {
                pendingMutex.withLock { pendingSignals.remove(sessionId)?.cancel() }
            }
            return false
        }
        if (!expectAck) return true
        val acknowledged = withTimeoutOrNull(ACK_TIMEOUT_MS) { deferred!!.await() } == true
        if (!acknowledged) {
            pendingMutex.withLock { pendingSignals.remove(sessionId)?.cancel() }
        }
        return acknowledged
    }

    suspend fun sendLinkRequest(contact: Contact): Boolean {
        if (contact.linkStatus == ContactLinkStatus.LINKED || contact.phone.isBlank()) return false
        val sent = sendLinkSignal(contact, TYPE_LINK_REQUEST)
        if (sent) {
            contactRepository.upsert(contact.copy(linkStatus = ContactLinkStatus.LINK_PENDING))
        }
        return sent
    }

    private suspend fun sendLinkSignal(contact: Contact, type: String): Boolean {
        val timestamp = timeProvider.nowMillis()
        val parts = mutableListOf(
            CONTACT_SIGNAL_HEADER,
            "$KEY_TYPE=$type",
            "$KEY_EMERGENCY=0",
            "$KEY_TIMESTAMP=$timestamp",
            "$KEY_PLAN=${localPlanTier.name}"
        )
        val payload = parts.joinToString(separator = "|")
        val friendly = linkMessageFor(type)
        val body = buildString {
            append(CONTACT_SIGNAL_PREFIX)
            append(' ')
            append(payload)
            if (friendly.isNotEmpty()) {
                append('\n')
                append(friendly)
            }
        }
        return dispatcher.sendSms(body, listOf(contact)) > 0
    }

    private fun linkMessageFor(type: String): String = when (type) {
        TYPE_LINK_REQUEST -> "SafeWord link request: let's connect to keep each other safe."
        TYPE_LINK_RESPONSE -> "SafeWord link confirmed. SafeWord alerts are now shared."
        else -> ""
    }

    suspend fun sendInvite(contact: Contact): Boolean {
        val sent = sendContactSignal(contact, ContactEngagementType.INVITE, emergency = false)
        if (sent) {
            val updated = contact.copy(linkStatus = ContactLinkStatus.LINK_PENDING)
            contactRepository.upsert(updated)
        }
        return sent
    }

    private fun buildInviteMessage(): String {
        val tierLabel = if (localPlanTier == PlanTier.PRO) "SafeWord Pro" else "SafeWord"
        return "I'm using $tierLabel to stay protected. Download the app and we'll link automatically: $DOWNLOAD_URL"
    }

    private fun buildInviteReceivedMessage(contactName: String, planTier: PlanTier?): String {
        val tierLabel = when (planTier) {
            PlanTier.PRO -> "SafeWord Pro"
            PlanTier.FREE -> "SafeWord"
            null -> "SafeWord"
        }
        return "$contactName invited you to connect on $tierLabel.\nOpen SafeWord to finish linking."
    }

    private suspend fun applyAlertProfile(profile: AlertProfile) {
        if (profile.boostRinger) {
            dispatcher.raiseRinger()
        }
        dispatcher.playSiren(false)
        when (profile.sound) {
            AlertSound.SIREN -> dispatcher.playSiren(true)
            AlertSound.GENTLE -> dispatcher.playGentleTone()
            AlertSound.SILENT -> Unit
        }
    }

    private suspend fun triggerEmergency(
        detectedWord: String,
        source: AlertSource,
        broadcast: Boolean = true,
        emergency: Boolean = true
    ) {
        val settings = dashboardState.value.settings ?: return
        val contacts = dashboardState.value.contacts ?: emptyList()
        val timestamp = timeProvider.nowMillis()

        val alertProfile = if (emergency) settings.emergencyAlert else settings.nonEmergencyAlert
        applyAlertProfile(alertProfile)

        val location = if (settings.includeLocation) dispatcher.resolveLocation() else null
        val locationText = location?.let { " https://maps.google.com/?q=${it.first},${it.second}" } ?: ""
        val message = buildString {
            if (emergency) {
                append("SafeWord emergency alert (${source.name}): \"$detectedWord\".")
                append(" Please check in immediately.")
            } else {
                append("SafeWord check-in (${source.name}): \"$detectedWord\".")
                append(" This is a non-emergency message.")
            }
            append(locationText)
        }
        val notifiedCount = dispatcher.sendSms(message, contacts)
        if (emergency) {
            dispatcher.showEmergencyPrompt(detectedWord)
        } else {
            dispatcher.showCheckInPrompt("SafeWord", message)
        }

        val event = AlertEvent(
            source = source,
            detectedWord = detectedWord,
            timestampMillis = timestamp,
            locationLat = location?.first,
            locationLon = location?.second,
            smsSent = notifiedCount > 0,
            contactsNotified = notifiedCount
        )
        val recorded = alertEventRepository.record(event)
        dispatcher.logEvent(recorded)

        if (broadcast) {
            peerBridge.broadcast(PeerBridgeEvent.AlertBroadcast(payload = recorded))
        }
    }

    private suspend fun handleIncomingPeerAlert(alert: AlertEvent) {
        val contacts = dashboardState.value.contacts ?: emptyList()
        val settingsSnapshot = dashboardState.value.settings ?: Defaults.settings
        applyAlertProfile(settingsSnapshot.emergencyAlert)

        val notified = dispatcher.sendSms(
            message = "SafeWord peer alert (${alert.source.name}): \"${alert.detectedWord}\" from linked device.",
            contacts = contacts
        )

        val recorded = alertEventRepository.record(
            alert.copy(
                id = null,
                source = AlertSource.PEER,
                timestampMillis = timeProvider.nowMillis(),
                contactsNotified = notified,
                smsSent = notified > 0
            )
        )
        dispatcher.logEvent(recorded)
    }

    private suspend fun handleIncomingCheckIn(event: PeerBridgeEvent.CheckIn) {
        val settingsSnapshot = dashboardState.value.settings ?: Defaults.settings
        applyAlertProfile(settingsSnapshot.nonEmergencyAlert)
        dispatcher.showCheckInPrompt(event.contactName, event.message)
    }

    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun handleIncomingContactSignal(senderPhone: String?, message: String) {
        val phone = senderPhone ?: return
        val encoded = message.substringAfter(CONTACT_SIGNAL_PREFIX).lineSequence().firstOrNull()?.trim() ?: return
        if (!encoded.startsWith(CONTACT_SIGNAL_HEADER)) return
        val data = encoded.split("|")
            .drop(1)
            .mapNotNull { token ->
                val parts = token.split("=")
                if (parts.size == 2) parts[0] to parts[1] else null
            }
            .toMap()
        val rawType = data[KEY_TYPE] ?: return
        val type = runCatching { ContactEngagementType.valueOf(rawType) }.getOrNull()
        val emergency = data[KEY_EMERGENCY]?.toIntOrNull() == 1
        val lat = data[KEY_LAT]?.toDoubleOrNull()
        val lon = data[KEY_LON]?.toDoubleOrNull()
        val planTier = data[KEY_PLAN]?.let { runCatching { PlanTier.valueOf(it) }.getOrNull() }
        val state = data[KEY_STATE]
        val sessionId = data[KEY_SESSION]
        val messageText = data[KEY_MESSAGE]
            ?.let { runCatching { Base64.Default.decode(it) }.getOrNull() }
            ?.decodeToString()
            ?.trim()
            ?.takeIf { it.isNotEmpty() }

        if (state == STATE_ACK && sessionId != null) {
            pendingMutex.withLock {
                pendingSignals.remove(sessionId)?.complete(true)
            }
            return
        }

        val existing = contactRepository.findByPhone(phone)
        val baseContact = existing ?: Contact(
            id = null,
            name = phone,
            phone = phone,
            email = null,
            createdAtMillis = timeProvider.nowMillis(),
            linkStatus = ContactLinkStatus.UNLINKED,
            planTier = planTier
        )

        val resolvedStatus = when (rawType) {
            TYPE_LINK_REQUEST, TYPE_LINK_RESPONSE -> ContactLinkStatus.LINKED
            else -> when (baseContact.linkStatus) {
                ContactLinkStatus.UNLINKED -> if (type != null) ContactLinkStatus.LINKED else ContactLinkStatus.UNLINKED
                else -> baseContact.linkStatus
            }
        }

        val resolved = contactRepository.upsert(
            baseContact.copy(
                linkStatus = resolvedStatus,
                planTier = planTier ?: baseContact.planTier
            )
        )

        val settingsSnapshot = dashboardState.value.settings ?: Defaults.settings

        when (rawType) {
            TYPE_LINK_REQUEST -> {
                dispatcher.raiseRinger()
                dispatcher.playGentleTone()
                dispatcher.showLinkNotification(resolved.name, "SafeWord link request received. You are now connected.")
                sendLinkSignal(resolved, TYPE_LINK_RESPONSE)
                return
            }
            TYPE_LINK_RESPONSE -> {
                dispatcher.raiseRinger()
                dispatcher.playGentleTone()
                dispatcher.showLinkNotification(resolved.name, "SafeWord link confirmed with ${resolved.name}.")
                return
            }
        }

        val descriptor = if (emergency) {
            "Emergency ${type?.name?.lowercase() ?: "message"}"
        } else {
            "Check-in ${type?.name?.lowercase() ?: "message"}"
        }
        val alertProfile = if (emergency) settingsSnapshot.emergencyAlert else settingsSnapshot.nonEmergencyAlert
        applyAlertProfile(alertProfile)
        val locationText = if (lat != null && lon != null) "\nhttps://maps.google.com/?q=$lat,$lon" else ""
        val prompt = when (type) {
            ContactEngagementType.INVITE -> buildInviteReceivedMessage(resolved.name, planTier)
            else -> buildString {
                append("$descriptor from ${resolved.name}.")
                if (messageText != null) {
                    append("\n")
                    append(messageText)
                }
                append(locationText)
            }
        }
        dispatcher.showCheckInPrompt(resolved.name, prompt)
        if (sessionId != null) {
            sendAcknowledgement(resolved, rawType, emergency, sessionId)
        }
    }

    private suspend fun sendAcknowledgement(
        contact: Contact,
        type: String,
        emergency: Boolean,
        sessionId: String
    ) {
        val parts = listOf(
            CONTACT_SIGNAL_HEADER,
            "$KEY_TYPE=$type",
            "$KEY_EMERGENCY=${if (emergency) 1 else 0}",
            "$KEY_TIMESTAMP=${timeProvider.nowMillis()}",
            "$KEY_PLAN=${localPlanTier.name}",
            "$KEY_STATE=$STATE_ACK",
            "$KEY_SESSION=$sessionId"
        )
        val payload = parts.joinToString(separator = "|")
        val body = buildString {
            append(CONTACT_SIGNAL_PREFIX)
            append(' ')
            append(payload)
            append('\n')
            append("SafeWord confirmation received.")
        }
        dispatcher.sendSms(body, listOf(contact))
    }

    companion object {
        private const val CONTACT_SIGNAL_PREFIX = "[SAFEWORD]"
        private const val CONTACT_SIGNAL_HEADER = "CONTACT"
        private const val KEY_TYPE = "TYPE"
        private const val KEY_EMERGENCY = "EMERGENCY"
        private const val KEY_TIMESTAMP = "TS"
        private const val KEY_LAT = "LAT"
        private const val KEY_LON = "LON"
        private const val KEY_PLAN = "PLAN"
        private const val KEY_MESSAGE = "MSG"
        private const val KEY_STATE = "STATE"
        private const val KEY_SESSION = "SID"
        private const val STATE_REQUEST = "REQ"
        private const val STATE_ACK = "ACK"
        private const val ACK_TIMEOUT_MS = 15_000L
        private const val DOWNLOAD_URL = "https://safeword.app/download"
        private const val TYPE_LINK_REQUEST = "LINK_REQUEST"
        private const val TYPE_LINK_RESPONSE = "LINK_RESPONSE"
    }
}

data class DashboardState(
    val settings: SafeWordSettings? = null,
    val contacts: List<Contact>? = null,
    val bridgeState: PeerBridgeState = PeerBridgeState.Idle
)
