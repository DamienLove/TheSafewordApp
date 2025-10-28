package com.safeword.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class AlertSound {
    SILENT,
    GENTLE,
    SIREN
}

@Serializable
data class AlertProfile(
    val sound: AlertSound = AlertSound.SIREN,
    val boostRinger: Boolean = true
)

@Serializable
data class SafeWordSettings(
    val safeWordOne: String,
    val safeWordTwo: String,
    val sensitivity: Int,
    val listeningEnabled: Boolean,
    val includeLocation: Boolean,
    val emergencyAlert: AlertProfile = AlertProfile(),
    val nonEmergencyAlert: AlertProfile = AlertProfile(sound = AlertSound.GENTLE, boostRinger = false),
    val testMode: Boolean
) {
    init {
        require(sensitivity in 1..100) { "Sensitivity must be between 1 and 100." }
    }

    fun safeWords(): List<String> = listOfNotNull(
        safeWordOne.takeIf { it.isNotBlank() },
        safeWordTwo.takeIf { it.isNotBlank() }
    )

    val isConfigured: Boolean get() = safeWords().isNotEmpty()
}
