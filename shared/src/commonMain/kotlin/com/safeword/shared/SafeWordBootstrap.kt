package com.safeword.shared

import com.safeword.shared.bridge.PeerBridge
import com.safeword.shared.domain.SafeWordEngine
import com.safeword.shared.domain.model.PlanTier
import com.safeword.shared.domain.repository.AlertEventRepository
import com.safeword.shared.domain.repository.ContactRepository
import com.safeword.shared.domain.repository.SettingsGateway
import com.safeword.shared.domain.service.EmergencyDispatcher
import com.safeword.shared.util.TimeProvider
import kotlinx.coroutines.CoroutineScope

object SafeWordBootstrap {
    fun createEngine(
        settingsGateway: SettingsGateway,
        contactRepository: ContactRepository,
        alertEventRepository: AlertEventRepository,
        emergencyDispatcher: EmergencyDispatcher,
        peerBridge: PeerBridge,
        timeProvider: TimeProvider,
        scope: CoroutineScope,
        planTier: PlanTier
    ): SafeWordEngine {
        return SafeWordEngine(
            settingsGateway = settingsGateway,
            contactRepository = contactRepository,
            alertEventRepository = alertEventRepository,
            dispatcher = emergencyDispatcher,
            peerBridge = peerBridge,
            timeProvider = timeProvider,
            scope = scope,
            localPlanTier = planTier
        )
    }
}
