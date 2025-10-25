package com.safeword.util

import android.content.Context
import android.media.AudioManager
import androidx.core.content.getSystemService

class RingerManager(context: Context) {
    private val audioManager: AudioManager? = context.getSystemService()

    fun raiseToMax() {
        audioManager?.let { manager ->
            val max = manager.getStreamMaxVolume(AudioManager.STREAM_RING)
            manager.setStreamVolume(AudioManager.STREAM_RING, max, AudioManager.FLAG_SHOW_UI)
        }
    }
}
