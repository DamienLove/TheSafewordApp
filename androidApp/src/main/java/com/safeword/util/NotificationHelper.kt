package com.safeword.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.safeword.R
import com.safeword.receiver.AlarmSilenceReceiver

class NotificationHelper(
    private val context: Context
) {

    private val manager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun ensureChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val listening = NotificationChannel(
                CHANNEL_LISTENING,
                context.getString(R.string.notification_channel_listening),
                NotificationManager.IMPORTANCE_LOW
            )
            val alerts = NotificationChannel(
                CHANNEL_ALERTS,
                context.getString(R.string.notification_channel_alerts),
                NotificationManager.IMPORTANCE_HIGH
            )
            val peer = NotificationChannel(
                CHANNEL_PEER,
                context.getString(R.string.notification_channel_peer),
                NotificationManager.IMPORTANCE_MIN
            )
            val checkIn = NotificationChannel(
                CHANNEL_CHECK_IN,
                context.getString(R.string.notification_channel_check_in),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(listening)
            manager.createNotificationChannel(alerts)
            manager.createNotificationChannel(peer)
            manager.createNotificationChannel(checkIn)
        }
    }

    fun buildListeningNotification(isActive: Boolean): Notification =
        NotificationCompat.Builder(context, CHANNEL_LISTENING)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(
                if (isActive) context.getString(R.string.notification_title_listening)
                else context.getString(R.string.listening_off)
            )
            .setOngoing(true)
            .build()

    fun buildAlertNotification(detectedWord: String): Notification =
        NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(context.getString(R.string.notification_title_alert))
            .setContentText(
                context.getString(R.string.safe_word_detected) + ": \"$detectedWord\""
            )
            .addAction(
                R.drawable.ic_stat_name,
                context.getString(R.string.silence_alarm),
                silenceAlarmPendingIntent()
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

    fun buildPeerNotification(stateDescription: String): Notification =
        NotificationCompat.Builder(context, CHANNEL_PEER)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(context.getString(R.string.notification_title_peer))
            .setContentText(stateDescription)
            .setOngoing(true)
            .build()

    fun buildCheckInNotification(contactName: String, message: String): Notification =
        NotificationCompat.Builder(context, CHANNEL_CHECK_IN)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(context.getString(R.string.notification_title_check_in, contactName))
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

    fun cancelAlertNotification() {
        manager.cancel(NOTIFICATION_ID_ALERT)
    }

    private fun silenceAlarmPendingIntent(): PendingIntent {
        val intent = Intent(context, AlarmSilenceReceiver::class.java).apply {
            action = AlarmSilenceReceiver.ACTION_SILENCE_ALARM
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(context, 0, intent, flags)
    }

    companion object {
        const val CHANNEL_LISTENING = "safeword_listening"
        const val CHANNEL_ALERTS = "safeword_alerts"
        const val CHANNEL_PEER = "safeword_peer"
        const val CHANNEL_CHECK_IN = "safeword_check_in"

        const val NOTIFICATION_ID_LISTENING = 1
        const val NOTIFICATION_ID_ALERT = 2
        const val NOTIFICATION_ID_PEER = 3
        const val NOTIFICATION_ID_CHECK_IN = 4
    }
}
