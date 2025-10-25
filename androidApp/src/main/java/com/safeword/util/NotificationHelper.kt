package com.safeword.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.safeword.R

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
            manager.createNotificationChannel(listening)
            manager.createNotificationChannel(alerts)
            manager.createNotificationChannel(peer)
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

    companion object {
        const val CHANNEL_LISTENING = "safeword_listening"
        const val CHANNEL_ALERTS = "safeword_alerts"
        const val CHANNEL_PEER = "safeword_peer"

        const val NOTIFICATION_ID_LISTENING = 1
        const val NOTIFICATION_ID_ALERT = 2
        const val NOTIFICATION_ID_PEER = 3
    }
}

