package com.safeword.util

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.safeword.R
import com.safeword.service.EmergencyHandlerService
import com.safeword.shared.domain.model.SafeWordSettings
import com.safeword.ui.MainActivity
import javax.inject.Inject

class AssistantShortcutUpdater @Inject constructor(
    private val context: Context
) {
    private var cachedPrimary: String? = null
    private var cachedSecondary: String? = null

    fun update(settings: SafeWordSettings) {
        val primary = settings.safeWordOne.trim()
        val secondary = settings.safeWordTwo.trim()
        if (primary == cachedPrimary && secondary == cachedSecondary) {
            return
        }
        cachedPrimary = primary
        cachedSecondary = secondary

        val shortcuts = mutableListOf<ShortcutInfoCompat>()

        primary.takeIf { it.isNotBlank() }?.let { phrase ->
            shortcuts += buildShortcut(
                id = ID_PRIMARY,
                label = phrase,
                isEmergency = true,
                phrase = phrase
            )
        }

        secondary.takeIf { it.isNotBlank() }?.let { phrase ->
            shortcuts += buildShortcut(
                id = ID_SECONDARY,
                label = phrase,
                isEmergency = false,
                phrase = phrase
            )
        }

        ShortcutManagerCompat.removeDynamicShortcuts(context, listOf(ID_PRIMARY, ID_SECONDARY))
        if (shortcuts.isNotEmpty()) {
            ShortcutManagerCompat.addDynamicShortcuts(context, shortcuts)
        }
    }

    private fun buildShortcut(
        id: String,
        label: String,
        isEmergency: Boolean,
        phrase: String
    ): ShortcutInfoCompat {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = MainActivity.ACTION_TRIGGER_SAFEWORD_SHORTCUT
            putExtra(MainActivity.EXTRA_SAFEWORD_PHRASE, phrase)
            putExtra(MainActivity.EXTRA_SAFEWORD_EMERGENCY, isEmergency)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val longLabel = if (isEmergency) {
            context.getString(R.string.shortcut_safe_word_emergency, label)
        } else {
            context.getString(R.string.shortcut_safe_word_checkin, label)
        }
        return ShortcutInfoCompat.Builder(context, id)
            .setShortLabel(label)
            .setLongLabel(longLabel)
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_logo_safeword))
            .setIntent(intent)
            .build()
    }

    companion object {
        private const val ID_PRIMARY = "safeword_primary"
        private const val ID_SECONDARY = "safeword_secondary"
    }
}
