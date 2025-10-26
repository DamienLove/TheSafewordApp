package com.safeword.service

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.safeword.shared.domain.model.AlertEvent
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.service.EmergencyDispatcher
import com.safeword.util.LocationHelper
import com.safeword.util.NotificationHelper
import com.safeword.util.RingerManager
import com.safeword.util.SmsSender
import com.safeword.util.SoundPlayer

class AndroidEmergencyDispatcher(
    private val context: Context,
    private val notificationHelper: NotificationHelper,
    private val ringerManager: RingerManager,
    private val soundPlayer: SoundPlayer,
    private val smsSender: SmsSender,
    private val locationHelper: LocationHelper
) : EmergencyDispatcher {

    override suspend fun raiseRinger() {
        ringerManager.raiseToMax()
    }

    override suspend fun playSiren(enabled: Boolean) {
        if (enabled) soundPlayer.playAlarm() else soundPlayer.stop()
    }

    override suspend fun playGentleTone() {
        ringerManager.raiseToLevel(RingerManager.GENTLE_ALERT_FRACTION)
        soundPlayer.playGentlePing()
    }

    override suspend fun resolveLocation(): Pair<Double, Double>? = locationHelper.getLastKnownLocation()

    override suspend fun sendSms(message: String, contacts: List<Contact>): Int =
        smsSender.send(message, contacts)

    override suspend fun showEmergencyPrompt(detectedWord: String) {
        val notification = notificationHelper.buildAlertNotification(detectedWord)
        NotificationManagerCompat.from(context)
            .notify(NotificationHelper.NOTIFICATION_ID_ALERT, notification)
    }

    override suspend fun showCheckInPrompt(contactName: String, message: String) {
        val notification = notificationHelper.buildCheckInNotification(contactName, message)
        NotificationManagerCompat.from(context)
            .notify(NotificationHelper.NOTIFICATION_ID_CHECK_IN, notification)
    }

    override suspend fun logEvent(event: AlertEvent) {
        // Could persist to analytics/logcat here. For now we keep the notification visible.
    }
}
