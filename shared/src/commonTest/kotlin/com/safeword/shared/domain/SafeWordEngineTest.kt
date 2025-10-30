package com.safeword.shared.domain

import com.safeword.shared.bridge.PeerBridge
import com.safeword.shared.bridge.model.PeerBridgeEvent
import com.safeword.shared.bridge.model.PeerBridgeState
import com.safeword.shared.domain.model.AlertEvent
import com.safeword.shared.domain.model.Contact
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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SafeWordEngineTest {

    @Test
    fun sendCheckInFallsBackToSmsWhenPeersUnavailable() = runBlocking {
        val environment = TestEnvironment(sendSmsResult = 1)
        try {
            environment.awaitDashboardReady()

            val result = environment.engine.sendCheckIn(environment.contact)

            assertTrue(result, "Ping should report success when SMS dispatch succeeds.")
            assertEquals(1, environment.dispatcher.sentMessages.size)
            val payload = environment.dispatcher.sentMessages.first()
            assertTrue(payload.contains("[SAFEWORD]"), "Payload should include SafeWord header.")
            assertTrue(payload.contains("Ping from SafeWord"), "Payload should describe the ping.")
        } finally {
            environment.close()
        }
    }

    @Test
    fun sendCheckInReturnsFalseWhenSmsFails() = runBlocking {
        val environment = TestEnvironment(sendSmsResult = 0)
        try {
            environment.awaitDashboardReady()

            val result = environment.engine.sendCheckIn(environment.contact)

            assertFalse(result, "Ping should fail when the SMS cannot be sent.")
            assertTrue(environment.dispatcher.sentMessages.isEmpty(), "No SMS payload should be recorded on failure.")
        } finally {
            environment.close()
        }
    }

    @Test
    fun sendCheckInBootstrapsLinkForUnlinkedContacts() = runBlocking {
        val environment = TestEnvironment(sendSmsResult = 1, linkStatus = ContactLinkStatus.UNLINKED)
        try {
            environment.awaitDashboardReady()

            val result = environment.engine.sendCheckIn(environment.contact)

            assertFalse(result, "Ping should report failure while SafeWord re-establishes the link.")
            assertEquals(2, environment.dispatcher.sentMessages.size, "Ping plus link invite should be emitted.")
            assertTrue(
                environment.dispatcher.linkNotifications.any { (_, message) ->
                    message.contains("invite", ignoreCase = true)
                },
                "Sender should be notified that a link invite was sent."
            )
        } finally {
            environment.close()
        }
    }

    private class TestEnvironment(
        sendSmsResult: Int,
        linkStatus: ContactLinkStatus = ContactLinkStatus.LINKED
    ) {
        val contact: Contact = Contact(
            id = 1L,
            name = "Casey Jones",
            phone = "+15551234567",
            email = null,
            createdAtMillis = 0L,
            linkStatus = linkStatus,
            planTier = PlanTier.FREE
        )

        private val settingsFlow = MutableStateFlow(
            SafeWordSettings(
                safeWordOne = "red",
                safeWordTwo = "",
                sensitivity = 50,
                listeningEnabled = true,
                includeLocation = false,
                testMode = false
            )
        )
        private val contactsFlow = MutableStateFlow(listOf(contact))
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

        val dispatcher = FakeEmergencyDispatcher(sendSmsResult)
        private val contactRepository = FakeContactRepository(contactsFlow)
        private val alertRepository = FakeAlertEventRepository()
        private val peerBridge = FakePeerBridge()
        private val timeProvider = FakeTimeProvider()
        private val settingsGateway = FakeSettingsGateway(settingsFlow)

        val engine = SafeWordEngine(
            settingsGateway = settingsGateway,
            contactRepository = contactRepository,
            alertEventRepository = alertRepository,
            dispatcher = dispatcher,
            peerBridge = peerBridge,
            timeProvider = timeProvider,
            scope = scope,
            localPlanTier = PlanTier.FREE
        )

        suspend fun awaitDashboardReady() {
            engine.dashboardState.first { it.settings != null && it.contacts != null }
        }

        fun close() {
            scope.cancel()
        }
    }

    private class FakeSettingsGateway(
        private val backing: MutableStateFlow<SafeWordSettings>
    ) : SettingsGateway {
        override val settings: Flow<SafeWordSettings> = backing

        override suspend fun update(transform: (SafeWordSettings) -> SafeWordSettings) {
            backing.value = transform(backing.value)
        }
    }

    private class FakeContactRepository(
        private val contacts: MutableStateFlow<List<Contact>>
    ) : ContactRepository {
        override fun observeContacts(): Flow<List<Contact>> = contacts

        override suspend fun upsert(contact: Contact): Contact {
            val current = contacts.value.toMutableList()
            val index = current.indexOfFirst { it.id != null && it.id == contact.id }
            if (index >= 0) {
                current[index] = contact
            } else {
                current += contact
            }
            contacts.value = current
            return contact
        }

        override suspend fun delete(contactId: Long) {
            contacts.value = contacts.value.filterNot { it.id == contactId }
        }

        override suspend fun getContact(contactId: Long): Contact? =
            contacts.value.firstOrNull { it.id == contactId }

        override suspend fun findByPhone(phone: String): Contact? =
            contacts.value.firstOrNull { it.phone == phone }

        override suspend fun listContacts(): List<Contact> = contacts.value
    }

    private class FakeAlertEventRepository : AlertEventRepository {
        override fun observeLatest(limit: Int): Flow<List<AlertEvent>> = flowOf(emptyList())

        override suspend fun record(event: AlertEvent): AlertEvent = event
    }

    private class FakeEmergencyDispatcher(
        private val sendSmsResult: Int
    ) : EmergencyDispatcher {
        val sentMessages = mutableListOf<String>()
        val linkNotifications = mutableListOf<Pair<String, String>>()

        override suspend fun raiseRinger() = Unit

        override suspend fun playSiren(enabled: Boolean) = Unit

        override suspend fun playGentleTone() = Unit

        override suspend fun resolveLocation(): Pair<Double, Double>? = null

        override suspend fun sendSms(message: String, contacts: List<Contact>): Int {
            if (sendSmsResult > 0) {
                sentMessages += message
            }
            return sendSmsResult
        }

        override suspend fun showEmergencyPrompt(detectedWord: String) = Unit

        override suspend fun showCheckInPrompt(contactName: String, message: String) = Unit

        override suspend fun showLinkNotification(contactName: String, message: String) {
            linkNotifications += contactName to message
        }

        override suspend fun logEvent(event: AlertEvent) = Unit
    }

    private class FakePeerBridge : PeerBridge {
        private val stateFlow = MutableStateFlow<PeerBridgeState>(PeerBridgeState.Idle)
        private val eventsFlow = MutableSharedFlow<PeerBridgeEvent>(extraBufferCapacity = 4)

        override val state: Flow<PeerBridgeState> = stateFlow

        override val incomingEvents: Flow<PeerBridgeEvent> = eventsFlow

        override suspend fun startAdvertising() = Unit

        override suspend fun stop() = Unit

        override suspend fun broadcast(event: PeerBridgeEvent) = Unit
    }

    private class FakeTimeProvider : TimeProvider {
        private var current = 1_000L

        override fun nowMillis(): Long {
            current += 1
            return current
        }
    }
}
