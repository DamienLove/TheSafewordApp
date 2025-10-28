package com.safeword.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.safeword.shared.config.Defaults
import com.safeword.shared.domain.model.AlertProfile
import com.safeword.shared.domain.model.AlertSound
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
            prefs[EMERGENCY_SOUND] = updated.emergencyAlert.sound.name
            prefs[EMERGENCY_BOOST] = updated.emergencyAlert.boostRinger
            prefs[CHECKIN_SOUND] = updated.nonEmergencyAlert.sound.name
            prefs[CHECKIN_BOOST] = updated.nonEmergencyAlert.boostRinger
            prefs[TEST_MODE] = updated.testMode
            prefs.remove(PLAY_SIREN)
        }
    }

    private fun Preferences.toSettings(): SafeWordSettings = SafeWordSettings(
        safeWordOne = this[SAFE_WORD_ONE] ?: Defaults.settings.safeWordOne,
        safeWordTwo = this[SAFE_WORD_TWO] ?: Defaults.settings.safeWordTwo,
        sensitivity = this[SENSITIVITY] ?: Defaults.settings.sensitivity,
        listeningEnabled = this[LISTENING_ENABLED] ?: Defaults.settings.listeningEnabled,
        includeLocation = this[INCLUDE_LOCATION] ?: Defaults.settings.includeLocation,
        emergencyAlert = AlertProfile(
            sound = this[EMERGENCY_SOUND]?.let { runCatching { AlertSound.valueOf(it) }.getOrNull() } ?: Defaults.settings.emergencyAlert.sound,
            boostRinger = this[EMERGENCY_BOOST] ?: Defaults.settings.emergencyAlert.boostRinger
        ),
        nonEmergencyAlert = AlertProfile(
            sound = this[CHECKIN_SOUND]?.let { runCatching { AlertSound.valueOf(it) }.getOrNull() } ?: Defaults.settings.nonEmergencyAlert.sound,
            boostRinger = this[CHECKIN_BOOST] ?: Defaults.settings.nonEmergencyAlert.boostRinger
        ),
        testMode = this[TEST_MODE] ?: Defaults.settings.testMode
    )

    companion object {
        val SAFE_WORD_ONE = stringPreferencesKey("safe_word_1")
        val SAFE_WORD_TWO = stringPreferencesKey("safe_word_2")
        val SENSITIVITY = intPreferencesKey("sensitivity")
        val LISTENING_ENABLED = booleanPreferencesKey("listening_enabled")
        val INCLUDE_LOCATION = booleanPreferencesKey("include_location")
        val EMERGENCY_SOUND = stringPreferencesKey("emergency_sound")
        val EMERGENCY_BOOST = booleanPreferencesKey("emergency_boost")
        val CHECKIN_SOUND = stringPreferencesKey("checkin_sound")
        val CHECKIN_BOOST = booleanPreferencesKey("checkin_boost")
        val PLAY_SIREN = booleanPreferencesKey("play_siren")
        val TEST_MODE = booleanPreferencesKey("test_mode")
    }
}
