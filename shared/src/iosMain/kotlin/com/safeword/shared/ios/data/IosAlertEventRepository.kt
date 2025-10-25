package com.safeword.shared.ios.data

import com.safeword.shared.domain.model.AlertEvent
import com.safeword.shared.domain.repository.AlertEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

class IosAlertEventRepository(
    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults()
) : AlertEventRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private val state = MutableStateFlow(load())

    override fun observeLatest(limit: Int): Flow<List<AlertEvent>> = state.asStateFlow()
        .let { flow ->
            MutableStateFlow(flow.value.take(limit)).asStateFlow()
        }

    override suspend fun record(event: AlertEvent): AlertEvent {
        val nextId = (state.value.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
        val recorded = event.copy(id = event.id ?: nextId)
        val updated = (listOf(recorded) + state.value).sortedByDescending { it.timestampMillis }.take(50)
        state.value = updated
        userDefaults.setObject(json.encodeToString(updated), forKey = KEY)
        return recorded
    }

    private fun load(): List<AlertEvent> {
        val stored = userDefaults.stringForKey(KEY) ?: return emptyList()
        return runCatching { json.decodeFromString<List<AlertEvent>>(stored) }.getOrDefault(emptyList())
    }

    companion object {
        private const val KEY = "safeword_alert_events"
    }
}
