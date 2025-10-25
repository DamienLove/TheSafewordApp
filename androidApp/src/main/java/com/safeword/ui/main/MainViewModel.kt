package com.safeword.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safeword.shared.bridge.model.PeerBridgeState
import com.safeword.shared.domain.SafeWordEngine
import com.safeword.shared.domain.usecase.ToggleIncomingModeUseCase
import com.safeword.shared.domain.usecase.ToggleListeningUseCase
import com.safeword.shared.domain.usecase.ToggleTestModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val engine: SafeWordEngine,
    private val toggleListening: ToggleListeningUseCase,
    private val toggleIncomingMode: ToggleIncomingModeUseCase,
    private val toggleTestMode: ToggleTestModeUseCase
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = engine.dashboardState
        .map { dashboard ->
            MainUiState(
                listeningEnabled = dashboard.settings?.listeningEnabled ?: false,
                safeWordConfigured = dashboard.settings?.isConfigured ?: false,
                incomingMode = dashboard.settings?.incomingMode ?: true,
                contacts = dashboard.contacts?.size ?: 0,
                peerState = dashboard.bridgeState
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            MainUiState()
        )

    fun setListening(enabled: Boolean) {
        viewModelScope.launch { toggleListening(enabled) }
    }

    fun setModeIncoming(incoming: Boolean) {
        viewModelScope.launch { toggleIncomingMode(incoming) }
    }

    fun setTestMode(enabled: Boolean) {
        viewModelScope.launch { toggleTestMode(enabled) }
    }
}

data class MainUiState(
    val listeningEnabled: Boolean = false,
    val safeWordConfigured: Boolean = false,
    val incomingMode: Boolean = true,
    val contacts: Int = 0,
    val peerState: PeerBridgeState = PeerBridgeState.Idle
)
