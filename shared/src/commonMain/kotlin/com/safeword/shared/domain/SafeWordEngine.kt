package com.safeword.shared.domain

import com.safeword.shared.bridge.PeerBridge
import com.safeword.shared.bridge.model.PeerBridgeEvent
import com.safeword.shared.bridge.model.PeerBridgeState
import com.safeword.shared.domain.model.AlertEvent
import com.safeword.shared.domain.model.AlertSource
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.model.ContactEngagementType
import com.safeword.shared.domain.model.ContactLinkStatus
import com.safeword.shared.domain.model.PlanTier
import com.safeword.shared.domain.model.SafeWordSettings
import com.safeword.shared.domain.repository.AlertEventRepository
import com.safeword.shared.domain.repository.ContactRepository
import com.safeword.shared.domain.repository.SettingsGateway
import com.safeword.shared.domain.service.EmergencyDispatcher
import com.safeword.shared.util.TimeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    suspend fun sendContactSignal(
        contact: Contact,
        type: ContactEngagementType,
        emergency: Boolean
    ): Boolean {
        if (type != ContactEngagementType.INVITE && contact.linkStatus == ContactLinkStatus.UNLINKED) return false
        val settings = dashboardState.value.settings ?: return false
        val timestamp = timeProvider.nowMillis()
        val includeLocation = settings.includeLocation && type != ContactEngagementType.INVITE
        val location = if (includeLocation) dispatcher.resolveLocation() else null
        val parts = mutableListOf(
            CONTACT_SIGNAL_HEADER,
            "$KEY_TYPE=${type.name}",
            "$KEY_EMERGENCY=${if (emergency) 1 else 0}",
            "$KEY_TIMESTAMP=$timestamp",
            "$KEY_PLAN=${localPlanTier.name}"
        )
        location?.let { (lat, lon) ->
            parts += "$KEY_LAT=$lat"
            parts += "$KEY_LON=$lon"
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
                "$tone message request via SafeWord.$locationLink"
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
        val sent = dispatcher.sendSms(body, listOf(contact)) > 0
        return sent
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

    private suspend fun triggerEmergency(
        detectedWord: String,
        source: AlertSource,
        broadcast: Boolean = true,
        emergency: Boolean = true
    ) {
        val settings = dashboardState.value.settings ?: return
        val contacts = dashboardState.value.contacts ?: emptyList()
        val timestamp = timeProvider.nowMillis()

        dispatcher.raiseRinger()
        if (emergency && settings.playSiren) {
            dispatcher.playSiren(true)
        } else {
            dispatcher.playGentleTone()
        }

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
        dispatcher.raiseRinger()
        dispatcher.playSiren(true)

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
        dispatcher.playGentleTone()
        dispatcher.showCheckInPrompt(event.contactName, event.message)
    }

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
        dispatcher.raiseRinger()
        dispatcher.playGentleTone()
        val locationText = if (lat != null && lon != null) "\nhttps://maps.google.com/?q=$lat,$lon" else ""
        val prompt = when (type) {
            ContactEngagementType.INVITE -> buildInviteReceivedMessage(resolved.name, planTier)
            else -> "$descriptor from ${resolved.name}.$locationText"
        }
        dispatcher.showCheckInPrompt(resolved.name, prompt)
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
        private const val DOWNLOAD_URL = "https://safeword.app/download"
    }
}

data class DashboardState(
    val settings: SafeWordSettings? = null,
    val contacts: List<Contact>? = null,
    val bridgeState: PeerBridgeState = PeerBridgeState.Idle
)



