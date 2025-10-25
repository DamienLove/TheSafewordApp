package com.safeword.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.safeWordDataStore by preferencesDataStore(name = "safeword_prefs")

