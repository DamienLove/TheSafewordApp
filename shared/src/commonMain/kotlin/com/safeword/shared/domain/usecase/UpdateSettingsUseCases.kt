package com.safeword.shared.domain.usecase

import com.safeword.shared.domain.model.AlertProfile
import com.safeword.shared.domain.model.AlertSound
import com.safeword.shared.domain.model.SafeWordSettings
import com.safeword.shared.domain.repository.SettingsGateway

class UpdateSafeWordsUseCase(
    private val settingsGateway: SettingsGateway
) {
    suspend operator fun invoke(wordOne: String, wordTwo: String) {
        settingsGateway.update { existing ->
            existing.copy(
                safeWordOne = wordOne.trim(),
                safeWordTwo = wordTwo.trim()
            )
        }
    }
}

class UpdateSensitivityUseCase(
    private val settingsGateway: SettingsGateway
) {
    suspend operator fun invoke(level: Int) {
        val clamped = level.coerceIn(1, 100)
        settingsGateway.update { it.copy(sensitivity = clamped) }
    }
}

class ToggleListeningUseCase(
    private val settingsGateway: SettingsGateway
) {
    suspend operator fun invoke(enabled: Boolean) {
        settingsGateway.update { it.copy(listeningEnabled = enabled) }
    }
}

class ToggleIncludeLocationUseCase(
    private val settingsGateway: SettingsGateway
) {
    suspend operator fun invoke(include: Boolean) {
        settingsGateway.update { it.copy(includeLocation = include) }
    }
}

class UpdateEmergencyAlertProfileUseCase(
    private val settingsGateway: SettingsGateway
) {
    suspend operator fun invoke(sound: AlertSound, boostRinger: Boolean) {
        settingsGateway.update {
            it.copy(emergencyAlert = AlertProfile(sound = sound, boostRinger = boostRinger))
        }
    }
}

class UpdateCheckInAlertProfileUseCase(
    private val settingsGateway: SettingsGateway
) {
    suspend operator fun invoke(sound: AlertSound, boostRinger: Boolean) {
        settingsGateway.update {
            it.copy(nonEmergencyAlert = AlertProfile(sound = sound, boostRinger = boostRinger))
        }
    }
}

class ToggleTestModeUseCase(
    private val settingsGateway: SettingsGateway
) {
    suspend operator fun invoke(enabled: Boolean) {
        settingsGateway.update { it.copy(testMode = enabled) }
    }
}
