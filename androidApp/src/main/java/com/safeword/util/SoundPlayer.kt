package com.safeword.util

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build

class SoundPlayer(private val context: Context) {
    private var alarmPlayer: MediaPlayer? = null
    private var gentlePlayer: MediaPlayer? = null
    private var ringtone: Ringtone? = null

    fun playAlarm() {
        stopGentle()
        stopRingtone()
        stopAlarm()
        val customSoundFd = resolveCustomSound(CUSTOM_SOUND_NAME)
        if (customSoundFd != null) {
            alarmPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(customSoundFd.fileDescriptor, customSoundFd.startOffset, customSoundFd.length)
                isLooping = true
                prepare()
                start()
            }
            customSoundFd.close()
            return
        }

        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(context, uri)?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                isLooping = true
            }
            play()
        }
    }

    fun playGentlePing() {
        stopGentle()
        val customSoundFd = resolveCustomSound(GENTLE_SOUND_NAME)
        if (customSoundFd != null) {
            gentlePlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(customSoundFd.fileDescriptor, customSoundFd.startOffset, customSoundFd.length)
                isLooping = false
                setOnCompletionListener { stopGentle() }
                prepare()
                start()
            }
            customSoundFd.close()
            return
        }

        stopRingtone()
        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(context, uri)?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                isLooping = false
            }
            play()
        }
    }

    fun stop() {
        stopAlarm()
        stopGentle()
        stopRingtone()
    }

    fun isPlaying(): Boolean =
        alarmPlayer?.isPlaying == true || gentlePlayer?.isPlaying == true || ringtone?.isPlaying == true

    private fun stopAlarm() {
        alarmPlayer?.let { player ->
            runCatching {
                if (player.isPlaying) player.stop()
                player.reset()
                player.release()
            }
        }
        alarmPlayer = null
    }

    private fun stopGentle() {
        gentlePlayer?.let { player ->
            runCatching {
                if (player.isPlaying) player.stop()
                player.reset()
                player.release()
            }
        }
        gentlePlayer = null
    }

    private fun stopRingtone() {
        ringtone?.let { tone ->
            if (tone.isPlaying) tone.stop()
        }
        ringtone = null
    }

    private fun resolveCustomSound(name: String): AssetFileDescriptor? {
        val resId = context.resources.getIdentifier(name, "raw", context.packageName)
        return if (resId != 0) context.resources.openRawResourceFd(resId) else null
    }

    companion object {
        private const val CUSTOM_SOUND_NAME = "safeword_alert"
        private const val GENTLE_SOUND_NAME = "safeword_ping"
    }
}
