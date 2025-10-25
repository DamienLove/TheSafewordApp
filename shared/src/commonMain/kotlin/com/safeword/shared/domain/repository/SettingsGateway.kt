package com.safeword.shared.domain.repository

import com.safeword.shared.domain.model.SafeWordSettings
import kotlinx.coroutines.flow.Flow

interface SettingsGateway {
    val settings: Flow<SafeWordSettings>

    suspend fun update(transform: (SafeWordSettings) -> SafeWordSettings)
}

