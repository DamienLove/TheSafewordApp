package com.safeword.shared.domain

import com.safeword.shared.bridge.PeerBridge
import com.safeword.shared.bridge.model.PeerBridgeEvent
import com.safeword.shared.bridge.model.PeerBridgeState
import com.safeword.shared.domain.model.AlertEvent
import com.safeword.shared.domain.model.AlertSource
import com.safeword.shared.domain.model.Contact
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
        val normalised = transcript.lowercase().trim()
        val match = settings.safeWords().firstOrNull { normalised.contains(it.lowercase()) }
        if (match != null) {
            triggerEmergency(match, AlertSource.VOICE)
        }
    }

    suspend fun processSms(body: String) {
        val settings = dashboardState.value.settings ?: return
        if (!settings.incomingMode || settings.safeWords().isEmpty()) return
        val normalised = body.lowercase().trim()
        val match = settings.safeWords().firstOrNull { normalised.contains(it.lowercase()) }
        if (match != null) {
            triggerEmergency(match, AlertSource.SMS)
        }
    }

    suspend fun runTest() {
        triggerEmergency(detectedWord = "TEST", source = AlertSource.TEST, broadcast = false)
    }

    suspend fun triggerManual(word: String, source: AlertSource, broadcast: Boolean = true) {
        triggerEmergency(detectedWord = word, source = source, broadcast = broadcast)
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

    private suspend fun triggerEmergency(
        detectedWord: String,
        source: AlertSource,
        broadcast: Boolean = true
    ) {
        val settings = dashboardState.value.settings ?: return
        val contacts = dashboardState.value.contacts ?: emptyList()
        val timestamp = timeProvider.nowMillis()

        dispatcher.raiseRinger()
        dispatcher.playSiren(settings.playSiren)

        val location = if (settings.includeLocation) dispatcher.resolveLocation() else null
        val locationText = location?.let { " https://maps.google.com/?q=${it.first},${it.second}" } ?: ""
        val message = buildString {
            append("SafeWord alert (${source.name}): \"$detectedWord\".")
            append(" Please check in immediately.")
            append(locationText)
        }
        val notifiedCount = dispatcher.sendSms(message, contacts)
        dispatcher.showEmergencyPrompt(detectedWord)

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
}

data class DashboardState(
    val settings: SafeWordSettings? = null,
    val contacts: List<Contact>? = null,
    val bridgeState: PeerBridgeState = PeerBridgeState.Idle
)
