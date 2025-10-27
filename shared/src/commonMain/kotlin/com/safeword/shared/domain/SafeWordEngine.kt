package com.safeword.shared.domain

import com.safeword.shared.bridge.PeerBridge
import com.safeword.shared.bridge.model.PeerBridgeEvent
import com.safeword.shared.bridge.model.PeerBridgeState
import com.safeword.shared.domain.model.AlertEvent
import com.safeword.shared.domain.model.AlertSource
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.model.ContactEngagementType
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
    private val scope: CoroutineScope
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
        if (!contact.isSafewordPeer) return false
        val settings = dashboardState.value.settings ?: return false
        val timestamp = timeProvider.nowMillis()
        val location = if (settings.includeLocation) dispatcher.resolveLocation() else null
        val parts = mutableListOf(
            CONTACT_SIGNAL_HEADER,
            "$KEY_TYPE=${type.name}",
            "$KEY_EMERGENCY=${if (emergency) 1 else 0}",
            "$KEY_TIMESTAMP=$timestamp"
        )
        location?.let { (lat, lon) ->
            parts += "$KEY_LAT=$lat"
            parts += "$KEY_LON=$lon"
        }
        val payload = parts.joinToString(separator = "|")
        val locationLink = location?.let { " https://maps.google.com/?q=${it.first},${it.second}" } ?: ""
        val tone = if (emergency) "Emergency" else "Check-in"
        val friendly = "$tone ${type.name.lowercase()} request via SafeWord.$locationLink"
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
        val type = data[KEY_TYPE]?.let { runCatching { ContactEngagementType.valueOf(it) }.getOrNull() }
            ?: ContactEngagementType.CALL
        val emergency = data[KEY_EMERGENCY]?.toIntOrNull() == 1
        val lat = data[KEY_LAT]?.toDoubleOrNull()
        val lon = data[KEY_LON]?.toDoubleOrNull()

        val existing = contactRepository.findByPhone(phone)
        val resolved = when {
            existing == null -> contactRepository.upsert(
                Contact(
                    id = null,
                    name = phone,
                    phone = phone,
                    email = null,
                    createdAtMillis = timeProvider.nowMillis(),
                    isSafewordPeer = true
                )
            )
            existing.isSafewordPeer -> existing
            else -> contactRepository.upsert(existing.copy(isSafewordPeer = true))
        }

        dispatcher.raiseRinger()
        dispatcher.playGentleTone()
        val descriptor = if (emergency) "Emergency ${type.name.lowercase()}" else "Check-in ${type.name.lowercase()}"
        val locationText = if (lat != null && lon != null) "\nhttps://maps.google.com/?q=$lat,$lon" else ""
        dispatcher.showCheckInPrompt(resolved.name, "$descriptor from ${resolved.name}.$locationText")
    }

    companion object {
        private const val CONTACT_SIGNAL_PREFIX = "[SAFEWORD]"
        private const val CONTACT_SIGNAL_HEADER = "CONTACT"
        private const val KEY_TYPE = "TYPE"
        private const val KEY_EMERGENCY = "EMERGENCY"
        private const val KEY_TIMESTAMP = "TS"
        private const val KEY_LAT = "LAT"
        private const val KEY_LON = "LON"
    }
}

data class DashboardState(
    val settings: SafeWordSettings? = null,
    val contacts: List<Contact>? = null,
    val bridgeState: PeerBridgeState = PeerBridgeState.Idle
)
