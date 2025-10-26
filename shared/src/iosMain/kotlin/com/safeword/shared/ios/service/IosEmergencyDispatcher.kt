package com.safeword.shared.ios.service

import com.safeword.shared.domain.model.AlertEvent
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.service.EmergencyDispatcher
import platform.AVFAudio.AVAudioPlayer
import platform.AVFoundation.AVAudioSession
import platform.Foundation.NSURL
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

class IosEmergencyDispatcher : EmergencyDispatcher {

    private var player: AVAudioPlayer? = null

    override suspend fun raiseRinger() {
        runCatching {
            val session = AVAudioSession.sharedInstance()
            session.setCategory(AVAudioSessionCategoryPlayback, error = null)
            session.setActive(true, error = null)
        }
    }

    override suspend fun playSiren(enabled: Boolean) {
        if (!enabled) {
            player?.stop()
            player = null
            return
        }
        val url = NSURL.fileURLWithPath("/System/Library/Audio/UISounds/alarm.caf")
        player = AVAudioPlayer(contentsOfURL = url, error = null)?.apply { play() }
    }

    override suspend fun playGentleTone() {
        val url = NSURL.fileURLWithPath("/System/Library/Audio/UISounds/Modern/sms_alert_complete.caf")
        player?.stop()
        player = AVAudioPlayer(contentsOfURL = url, error = null)?.apply {
            setVolume(0.5f, 0f)
            play()
        }
    }

    override suspend fun resolveLocation(): Pair<Double, Double>? = null

    override suspend fun sendSms(message: String, contacts: List<Contact>): Int {
        showNotification("SafeWord prepared message", message)
        return 0
    }

    override suspend fun showEmergencyPrompt(detectedWord: String) {
        showNotification("SafeWord Alert", "Detected \"\"")
    }

    override suspend fun showCheckInPrompt(contactName: String, message: String) {
        showNotification("Check in request for ", message)
    }

    override suspend fun logEvent(event: AlertEvent) {
        // Could integrate with os_log if needed.
    }

    private fun showNotification(title: String, body: String) {
        dispatch_async(dispatch_get_main_queue()) {
            val content = UNMutableNotificationContent().apply {
                setTitle(title)
                setBody(body)
            }
            val request = UNNotificationRequest.requestWithIdentifier(
                identifier = "safeword.local.alert",
                content = content,
                trigger = null
            )
            UNUserNotificationCenter.currentNotificationCenter()
                .addNotificationRequest(request, withCompletionHandler = null)
        }
    }
}
