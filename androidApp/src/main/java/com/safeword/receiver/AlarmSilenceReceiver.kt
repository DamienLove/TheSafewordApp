package com.safeword.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.safeword.util.NotificationHelper
import com.safeword.util.SoundPlayer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmSilenceReceiver : BroadcastReceiver() {

    @Inject lateinit var soundPlayer: SoundPlayer
    @Inject lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_SILENCE_ALARM) {
            soundPlayer.stop()
            notificationHelper.cancelAlertNotification()
        }
    }

    companion object {
        const val ACTION_SILENCE_ALARM = "com.safeword.action.SILENCE_ALARM"
    }
}
