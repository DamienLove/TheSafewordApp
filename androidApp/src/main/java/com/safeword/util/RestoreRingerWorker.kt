package com.safeword.util

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Build
import androidx.core.content.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters

class RestoreRingerWorker(
    appContext: Context,
    params: WorkerParameters
) : Worker(appContext, params) {

    override fun doWork(): Result {
        val context = applicationContext
        val prefs = context.getSharedPreferences(RingerManager.PREFS_NAME, Context.MODE_PRIVATE)
        if (!prefs.contains(RingerManager.KEY_RING_VOLUME)) {
            return Result.success()
        }

        val audioManager: AudioManager? = context.getSystemService()
        val notificationManager: NotificationManager? = context.getSystemService()

        audioManager?.let { audio ->
            val ring = prefs.getInt(RingerManager.KEY_RING_VOLUME, audio.getStreamVolume(AudioManager.STREAM_RING))
            val notification = prefs.getInt(RingerManager.KEY_NOTIFICATION_VOLUME, audio.getStreamVolume(AudioManager.STREAM_NOTIFICATION))
            val alarm = prefs.getInt(RingerManager.KEY_ALARM_VOLUME, audio.getStreamVolume(AudioManager.STREAM_ALARM))
            val mode = prefs.getInt(RingerManager.KEY_RINGER_MODE, audio.ringerMode)

            audio.setStreamVolume(AudioManager.STREAM_RING, ring, 0)
            audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notification, 0)
            audio.setStreamVolume(AudioManager.STREAM_ALARM, alarm, 0)
            audio.ringerMode = mode
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager?.let { manager ->
                if (manager.isNotificationPolicyAccessGranted) {
                    val filter = prefs.getInt(
                        RingerManager.KEY_INTERRUPT_FILTER,
                        NotificationManager.INTERRUPTION_FILTER_ALL
                    )
                    manager.setInterruptionFilter(filter)
                }
            }
        }

        prefs.edit().clear().apply()
        return Result.success()
    }
}
