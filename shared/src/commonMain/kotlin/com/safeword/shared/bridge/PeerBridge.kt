package com.safeword.shared.bridge

import com.safeword.shared.bridge.model.PeerBridgeEvent
import com.safeword.shared.bridge.model.PeerBridgeState
import kotlinx.coroutines.flow.Flow

interface PeerBridge {
    val state: Flow<PeerBridgeState>
    val incomingEvents: Flow<PeerBridgeEvent>

    suspend fun startAdvertising()
    suspend fun stop()
    suspend fun broadcast(event: PeerBridgeEvent)
}
