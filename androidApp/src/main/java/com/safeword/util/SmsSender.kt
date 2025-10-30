package com.safeword.util

import android.telephony.SmsManager
import android.util.Log
import com.safeword.shared.domain.model.Contact

class SmsSender(private val smsManager: SmsManager = SmsManager.getDefault()) {
    fun send(message: String, contacts: List<Contact>): Int {
        var success = 0
        contacts.forEach { contact ->
            try {
                Log.i(TAG, "SafeWord SMS -> ${contact.phone} (length=${message.length})")
                smsManager.sendTextMessage(contact.phone, null, message, null, null)
                Log.i(TAG, "SafeWord SMS sent to ${contact.phone}")
                success++
            } catch (_: SecurityException) {
                // Permission denied, skip
                Log.e(TAG, "SafeWord SMS blocked (permission) -> ${contact.phone}")
            } catch (t: Throwable) {
                Log.e(TAG, "SafeWord SMS failed -> ${contact.phone}", t)
            }
        }
        return success
    }

    companion object {
        private const val TAG = "SafeWordSmsSender"
    }
}
