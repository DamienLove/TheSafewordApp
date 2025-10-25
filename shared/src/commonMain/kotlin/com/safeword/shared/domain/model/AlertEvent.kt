package com.safeword.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AlertEvent(
    val id: Long? = null,
    val source: AlertSource,
    val detectedWord: String,
    val timestampMillis: Long,
    val locationLat: Double? = null,
    val locationLon: Double? = null,
    val smsSent: Boolean,
    val contactsNotified: Int
)

enum class AlertSource {
    VOICE,
    SMS,
    TEST,
    PEER
}

