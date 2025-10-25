package com.safeword.util

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri

class SoundPlayer(private val context: Context) {
    private var ringtone: Ringtone? = null

    fun playAlarm() {
        stop()
        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(context, uri)?.apply {
            isLooping = true
            play()
        }
    }

    fun stop() {
        ringtone?.stop()
        ringtone = null
    }
}
