package com.safeword.util

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Build
import androidx.core.content.getSystemService
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.safeword.BuildConfig
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class RingerManager(private val context: Context) {
    private val audioManager: AudioManager? = context.getSystemService()
    private val notificationManager: NotificationManager? = context.getSystemService()
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun raiseToMax() {
        val audio = audioManager ?: return
        storeStateIfNeeded(audio, notificationManager)
        allowInterruptions()
        audio.ringerMode = AudioManager.RINGER_MODE_NORMAL
        val ringMax = audio.getStreamMaxVolume(AudioManager.STREAM_RING)
        audio.setStreamVolume(AudioManager.STREAM_RING, ringMax, AudioManager.FLAG_SHOW_UI)
        val notifMax = audio.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
        audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notifMax, AudioManager.FLAG_SHOW_UI)
        val alarmMax = audio.getStreamMaxVolume(AudioManager.STREAM_ALARM)
        audio.setStreamVolume(AudioManager.STREAM_ALARM, alarmMax, AudioManager.FLAG_SHOW_UI)
        scheduleRestore()
    }

    fun raiseToLevel(fraction: Float) {
        val audio = audioManager ?: return
        val clamped = fraction.coerceIn(0.2f, 1f)
        storeStateIfNeeded(audio, notificationManager)
        allowInterruptions()
        audio.ringerMode = AudioManager.RINGER_MODE_NORMAL
        val ringMax = audio.getStreamMaxVolume(AudioManager.STREAM_RING)
        val ringLevel = (ringMax * clamped).roundToInt().coerceAtLeast(1)
        audio.setStreamVolume(AudioManager.STREAM_RING, ringLevel, AudioManager.FLAG_SHOW_UI)
        val notifMax = audio.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
        val notifLevel = (notifMax * clamped).roundToInt().coerceAtLeast(1)
        audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notifLevel, AudioManager.FLAG_SHOW_UI)
        scheduleRestore()
    }

    private fun allowInterruptions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager?.let { manager ->
                if (manager.isNotificationPolicyAccessGranted) {
                    manager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                }
            }
        }
    }

    private fun storeStateIfNeeded(audio: AudioManager, notification: NotificationManager?) {
        if (prefs.contains(KEY_RING_VOLUME)) {
            return
        }
        val editor = prefs.edit()
        editor.putInt(KEY_RING_VOLUME, audio.getStreamVolume(AudioManager.STREAM_RING))
        editor.putInt(KEY_NOTIFICATION_VOLUME, audio.getStreamVolume(AudioManager.STREAM_NOTIFICATION))
        editor.putInt(KEY_ALARM_VOLUME, audio.getStreamVolume(AudioManager.STREAM_ALARM))
        editor.putInt(KEY_RINGER_MODE, audio.ringerMode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val filter = notification?.currentInterruptionFilter ?: NotificationManager.INTERRUPTION_FILTER_ALL
            editor.putInt(KEY_INTERRUPT_FILTER, filter)
        }
        editor.apply()
    }

    private fun scheduleRestore() {
        val delayMinutes = BuildConfig.RINGER_RESTORE_DELAY_MINUTES.coerceAtLeast(1L)
        val request = OneTimeWorkRequestBuilder<RestoreRingerWorker>()
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            RESTORE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    companion object {
        const val PREFS_NAME = "safeword_ringer_state"
        const val KEY_RING_VOLUME = "ring_volume"
        const val KEY_NOTIFICATION_VOLUME = "notification_volume"
        const val KEY_ALARM_VOLUME = "alarm_volume"
        const val KEY_RINGER_MODE = "ringer_mode"
        const val KEY_INTERRUPT_FILTER = "interrupt_filter"
        const val GENTLE_ALERT_FRACTION = 0.6f
        internal const val RESTORE_WORK_NAME = "restore_ringer_work"
    }
}
