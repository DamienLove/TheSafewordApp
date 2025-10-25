package com.safeword.shared.bridge.model

import com.safeword.shared.domain.model.AlertEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PeerBridgeState {
    @Serializable
    @SerialName("idle")
    data object Idle : PeerBridgeState

    @Serializable
    @SerialName("connected")
    data class Connected(val peerCount: Int) : PeerBridgeState

    @Serializable
    @SerialName("error")
    data class Error(val message: String) : PeerBridgeState
}

@Serializable
sealed interface PeerBridgeEvent {
    @Serializable
    @SerialName("alert")
    data class AlertBroadcast(val payload: AlertEvent) : PeerBridgeEvent

    @Serializable
    @SerialName("ack")
    data class Acknowledgement(val peerId: String, val alertId: Long?) : PeerBridgeEvent
}
