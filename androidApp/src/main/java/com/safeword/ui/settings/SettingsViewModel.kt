package com.safeword.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safeword.shared.config.Defaults
import com.safeword.shared.domain.SafeWordEngine
import com.safeword.shared.domain.model.SafeWordSettings
import com.safeword.shared.domain.repository.SettingsGateway
import com.safeword.shared.domain.usecase.ToggleIncludeLocationUseCase
import com.safeword.shared.domain.usecase.TogglePlaySirenUseCase
import com.safeword.shared.domain.usecase.ToggleTestModeUseCase
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
    private val togglePlaySiren: TogglePlaySirenUseCase,
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

    fun setPlaySiren(play: Boolean) {
        viewModelScope.launch { togglePlaySiren(play) }
    }

    fun setTestMode(enabled: Boolean) {
        viewModelScope.launch { toggleTestMode(enabled) }
    }

    fun runTest() {
        viewModelScope.launch { engine.runTest() }
    }
}
