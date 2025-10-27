package com.safeword.shared.ios

import com.safeword.shared.SafeWordBootstrap
import com.safeword.shared.bridge.PeerBridge
import com.safeword.shared.domain.SafeWordEngine
import com.safeword.shared.domain.model.PlanTier
import com.safeword.shared.domain.repository.AlertEventRepository
import com.safeword.shared.domain.repository.ContactRepository
import com.safeword.shared.domain.repository.SettingsGateway
import com.safeword.shared.domain.service.EmergencyDispatcher
import com.safeword.shared.ios.data.IosAlertEventRepository
import com.safeword.shared.ios.data.IosContactRepository
import com.safeword.shared.ios.service.IosEmergencyDispatcher
import IosUdpPeerBridge
import com.safeword.shared.ios.data.IosSettingsGateway
import com.safeword.shared.util.DefaultTimeProvider
import com.safeword.shared.util.TimeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class IosAppContainer @JvmOverloads constructor(
    peerBridge: PeerBridge? = null,
    private val emergencyDispatcher: EmergencyDispatcher = IosEmergencyDispatcher(),
    private val scope: CoroutineScope = MainScope(),
    private val timeProvider: TimeProvider = DefaultTimeProvider(),
    private val planTier: PlanTier = PlanTier.PRO
) {

    val coroutineScope: CoroutineScope get() = scope

    val settingsGateway: SettingsGateway = IosSettingsGateway()
    val contactRepository: ContactRepository = IosContactRepository()
    val alertEventRepository: AlertEventRepository = IosAlertEventRepository()
    private val bridge: PeerBridge = peerBridge ?: IosUdpPeerBridge(scope)

    val engine: SafeWordEngine = SafeWordBootstrap.createEngine(
        settingsGateway = settingsGateway,
        contactRepository = contactRepository,
        alertEventRepository = alertEventRepository,
        emergencyDispatcher = emergencyDispatcher,
        peerBridge = bridge,
        timeProvider = timeProvider,
        scope = scope,
        planTier = planTier
    )
}
