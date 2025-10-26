package com.safeword.shared.bridge

import com.safeword.shared.bridge.model.PeerBridgeEvent
import com.safeword.shared.bridge.model.PeerBridgeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class NoopPeerBridge : PeerBridge {
    private val stateFlow = MutableStateFlow<PeerBridgeState>(PeerBridgeState.Idle)
    private val eventsFlow = MutableSharedFlow<PeerBridgeEvent>()

    override val state: Flow<PeerBridgeState> = stateFlow.asStateFlow()
    override val incomingEvents: Flow<PeerBridgeEvent> = eventsFlow.asSharedFlow()

    override suspend fun startAdvertising() {
        stateFlow.value = PeerBridgeState.Connected(peerCount = 0)
    }

    override suspend fun stop() {
        stateFlow.value = PeerBridgeState.Idle
    }

    override suspend fun broadcast(event: PeerBridgeEvent) {
        // no-op
    }
}

