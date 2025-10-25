package com.safeword.shared.domain.repository

import com.safeword.shared.domain.model.AlertEvent
import kotlinx.coroutines.flow.Flow

interface AlertEventRepository {
    fun observeLatest(limit: Int = 20): Flow<List<AlertEvent>>
    suspend fun record(event: AlertEvent): AlertEvent
}

