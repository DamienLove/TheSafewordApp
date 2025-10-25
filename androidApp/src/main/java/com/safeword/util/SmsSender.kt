package com.safeword.util

import android.telephony.SmsManager
import com.safeword.shared.domain.model.Contact

class SmsSender(private val smsManager: SmsManager = SmsManager.getDefault()) {
    fun send(message: String, contacts: List<Contact>): Int {
        var success = 0
        contacts.forEach { contact ->
            try {
                smsManager.sendTextMessage(contact.phone, null, message, null, null)
                success++
            } catch (_: SecurityException) {
                // Permission denied, skip
            }
        }
        return success
    }
}
