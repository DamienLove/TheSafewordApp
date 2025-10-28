package com.safeword.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safeword.shared.bridge.model.PeerBridgeState
import com.safeword.shared.domain.SafeWordEngine
import com.safeword.shared.domain.model.ContactLinkStatus
import com.safeword.shared.domain.model.PlanTier
import com.safeword.shared.domain.usecase.ToggleListeningUseCase
import com.safeword.shared.domain.usecase.ToggleTestModeUseCase
import com.safeword.util.AssistantShortcutUpdater
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
    private val toggleTestMode: ToggleTestModeUseCase,
    private val assistantShortcutUpdater: AssistantShortcutUpdater
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = engine.dashboardState
        .map { dashboard ->
            dashboard.settings?.let { assistantShortcutUpdater.update(it) }
            MainUiState(
                listeningEnabled = dashboard.settings?.listeningEnabled ?: false,
                safeWordConfigured = dashboard.settings?.isConfigured ?: false,
                safewordContacts = dashboard.contacts?.count { it.linkStatus == ContactLinkStatus.LINKED } ?: 0,
                totalContacts = dashboard.contacts?.size ?: 0,
                linkedFreeContacts = dashboard.contacts?.count { it.linkStatus == ContactLinkStatus.LINKED && it.planTier == PlanTier.FREE } ?: 0,
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

    fun setTestMode(enabled: Boolean) {
        viewModelScope.launch { toggleTestMode(enabled) }
    }
}

data class MainUiState(
    val listeningEnabled: Boolean = false,
    val safeWordConfigured: Boolean = false,
    val safewordContacts: Int = 0,
    val totalContacts: Int = 0,
    val linkedFreeContacts: Int = 0,
    val peerState: PeerBridgeState = PeerBridgeState.Idle
)
