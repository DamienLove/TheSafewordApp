package com.safeword.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safeword.shared.config.Defaults
import com.safeword.shared.domain.SafeWordEngine
import com.safeword.shared.domain.model.AlertSound
import com.safeword.shared.domain.model.SafeWordSettings
import com.safeword.shared.domain.repository.SettingsGateway
import com.safeword.shared.domain.usecase.ToggleIncludeLocationUseCase
import com.safeword.shared.domain.usecase.ToggleTestModeUseCase
import com.safeword.shared.domain.usecase.UpdateCheckInAlertProfileUseCase
import com.safeword.shared.domain.usecase.UpdateEmergencyAlertProfileUseCase
import com.safeword.shared.domain.usecase.UpdateSafeWordsUseCase
import com.safeword.shared.domain.usecase.UpdateSensitivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    settingsGateway: SettingsGateway,
    private val updateSafeWords: UpdateSafeWordsUseCase,
    private val updateSensitivity: UpdateSensitivityUseCase,
    private val toggleIncludeLocation: ToggleIncludeLocationUseCase,
    private val updateEmergencyAlert: UpdateEmergencyAlertProfileUseCase,
    private val updateCheckInAlert: UpdateCheckInAlertProfileUseCase,
    private val toggleTestMode: ToggleTestModeUseCase,
    private val engine: SafeWordEngine
) : ViewModel() {

    val settings: StateFlow<SafeWordSettings> = settingsGateway.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Defaults.settings)

    fun saveSafeWords(first: String, second: String) {
        viewModelScope.launch { updateSafeWords(first, second) }
    }

    fun setSensitivity(value: Int) {
        viewModelScope.launch { updateSensitivity(value) }
    }

    fun setIncludeLocation(include: Boolean) {
        viewModelScope.launch { toggleIncludeLocation(include) }
    }

    fun setEmergencyAlertSound(sound: AlertSound) {
        viewModelScope.launch {
            val current = settings.value.emergencyAlert
            updateEmergencyAlert(sound, current.boostRinger)
        }
    }

    fun setEmergencyAlertBoost(boost: Boolean) {
        viewModelScope.launch {
            val current = settings.value.emergencyAlert
            updateEmergencyAlert(current.sound, boost)
        }
    }

    fun setCheckInAlertSound(sound: AlertSound) {
        viewModelScope.launch {
            val current = settings.value.nonEmergencyAlert
            updateCheckInAlert(sound, current.boostRinger)
        }
    }

    fun setCheckInAlertBoost(boost: Boolean) {
        viewModelScope.launch {
            val current = settings.value.nonEmergencyAlert
            updateCheckInAlert(current.sound, boost)
        }
    }

    fun setTestMode(enabled: Boolean) {
        viewModelScope.launch { toggleTestMode(enabled) }
    }

    fun runTest() {
        viewModelScope.launch { engine.runTest() }
    }
}
