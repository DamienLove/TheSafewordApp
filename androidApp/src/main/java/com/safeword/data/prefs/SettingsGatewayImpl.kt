package com.safeword.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.safeword.shared.config.Defaults
import com.safeword.shared.domain.model.SafeWordSettings
import com.safeword.shared.domain.repository.SettingsGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsGatewayImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsGateway {

    override val settings: Flow<SafeWordSettings> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences()) else throw exception
            }
            .map { prefs -> prefs.toSettings() }

    override suspend fun update(transform: (SafeWordSettings) -> SafeWordSettings) {
        dataStore.edit { prefs ->
            val current = prefs.toSettings()
            val updated = transform(current)
            prefs[SAFE_WORD_ONE] = updated.safeWordOne
            prefs[SAFE_WORD_TWO] = updated.safeWordTwo
            prefs[SENSITIVITY] = updated.sensitivity
            prefs[LISTENING_ENABLED] = updated.listeningEnabled
            prefs[INCLUDE_LOCATION] = updated.includeLocation
            prefs[PLAY_SIREN] = updated.playSiren
            prefs[TEST_MODE] = updated.testMode
        }
    }

    private fun Preferences.toSettings(): SafeWordSettings = SafeWordSettings(
        safeWordOne = this[SAFE_WORD_ONE] ?: Defaults.settings.safeWordOne,
        safeWordTwo = this[SAFE_WORD_TWO] ?: Defaults.settings.safeWordTwo,
        sensitivity = this[SENSITIVITY] ?: Defaults.settings.sensitivity,
        listeningEnabled = this[LISTENING_ENABLED] ?: Defaults.settings.listeningEnabled,
        includeLocation = this[INCLUDE_LOCATION] ?: Defaults.settings.includeLocation,
        playSiren = this[PLAY_SIREN] ?: Defaults.settings.playSiren,
        testMode = this[TEST_MODE] ?: Defaults.settings.testMode
    )

    companion object {
        val SAFE_WORD_ONE = stringPreferencesKey("safe_word_1")
        val SAFE_WORD_TWO = stringPreferencesKey("safe_word_2")
        val SENSITIVITY = intPreferencesKey("sensitivity")
        val LISTENING_ENABLED = booleanPreferencesKey("listening_enabled")
        val INCLUDE_LOCATION = booleanPreferencesKey("include_location")
        val PLAY_SIREN = booleanPreferencesKey("play_siren")
        val TEST_MODE = booleanPreferencesKey("test_mode")
    }
}
