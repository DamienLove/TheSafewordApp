package com.safeword.shared.config

import com.safeword.shared.domain.model.AlertProfile
import com.safeword.shared.domain.model.AlertSound
import com.safeword.shared.domain.model.SafeWordSettings

object Defaults {
    val settings = SafeWordSettings(
        safeWordOne = "",
        safeWordTwo = "",
        sensitivity = 50,
        listeningEnabled = false,
        includeLocation = true,
        emergencyAlert = AlertProfile(sound = AlertSound.SIREN, boostRinger = true),
        nonEmergencyAlert = AlertProfile(sound = AlertSound.GENTLE, boostRinger = false),
        testMode = false
    )
}
