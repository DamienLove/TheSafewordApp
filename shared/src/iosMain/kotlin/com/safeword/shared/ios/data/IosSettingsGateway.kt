package com.safeword.shared.ios.data

import com.safeword.shared.config.Defaults
import com.safeword.shared.domain.model.SafeWordSettings
import com.safeword.shared.domain.repository.SettingsGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

class IosSettingsGateway(
    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults()
) : SettingsGateway {

    private val json = Json { ignoreUnknownKeys = true }
    private val state = MutableStateFlow(load())

    override val settings: Flow<SafeWordSettings> = state.asStateFlow()

    override suspend fun update(transform: (SafeWordSettings) -> SafeWordSettings) {
        val updated = transform(state.value)
        state.value = updated
        userDefaults.setObject(json.encodeToString(updated), forKey = KEY)
    }

    private fun load(): SafeWordSettings {
        val stored = userDefaults.stringForKey(KEY)
        return stored?.let { runCatching { json.decodeFromString<SafeWordSettings>(it) }.getOrNull() } ?: Defaults.settings
    }

    companion object {
        private const val KEY = "safeword_settings_json"
    }
}
