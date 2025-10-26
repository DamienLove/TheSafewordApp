package com.safeword.util

import android.content.Context

object OnboardingPreferences {
    private const val PREFS_NAME = "safeword_onboarding"
    private const val KEY_COMPLETED = "completed"

    fun isCompleted(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_COMPLETED, false)
    }

    fun setCompleted(context: Context, completed: Boolean = true) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_COMPLETED, completed).apply()
    }
}
