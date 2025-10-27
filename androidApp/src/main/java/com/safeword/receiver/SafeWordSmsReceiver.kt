package com.safeword.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.PhoneNumberUtils
import android.util.Log
import com.safeword.BuildConfig
import com.safeword.shared.domain.SafeWordEngine
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.model.ContactLinkStatus
import com.safeword.shared.domain.repository.ContactRepository
import com.safeword.shared.domain.usecase.UpsertContactUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SafeWordSmsReceiver : BroadcastReceiver() {

    @Inject lateinit var engine: SafeWordEngine
    @Inject lateinit var upsertContactUseCase: UpsertContactUseCase
    @Inject lateinit var contactRepository: ContactRepository

    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        if (messages.isEmpty()) return

        val messageBody = messages.joinToString(separator = " ") { it.displayMessageBody }
        val sender = messages.firstOrNull()?.displayOriginatingAddress.orEmpty()
        val normalisedSender = PhoneNumberUtils.normalizeNumber(sender) ?: sender.filterNot { it.isWhitespace() }

        scope.launch {
            ensureContactForSender(sender, messageBody)
            engine.processSms(messageBody, normalisedSender.takeIf { it.isNotBlank() })
        }
    }

    private suspend fun ensureContactForSender(phone: String, message: String) {
        if (phone.isBlank()) return
        val normalisedPhone = PhoneNumberUtils.normalizeNumber(phone) ?: phone.filterNot { it.isWhitespace() }
        val existing = contactRepository.findByPhone(normalisedPhone)
        if (existing != null) {
            if (existing.linkStatus != ContactLinkStatus.LINKED) {
                runCatching {
                    upsertContactUseCase(existing.copy(linkStatus = ContactLinkStatus.LINKED))
                }.onFailure { Log.e(TAG, "Failed to upgrade contact link status for $normalisedPhone", it) }
            }
            return
        }

        val limit = BuildConfig.CONTACT_LIMIT
        if (limit > 0) {
            val currentSize = contactRepository.listContacts().size
            if (currentSize >= limit) {
                Log.w(TAG, "Contact limit reached, skipping auto-add for $phone")
                return
            }
        }

        val alias = safeWordNameRegex.find(message)?.groupValues?.getOrNull(1)?.takeIf { it.isNotBlank() }
        val contactName = alias ?: normalisedPhone
        val contact = Contact(
            id = null,
            name = contactName,
            phone = normalisedPhone,
            email = null,
            createdAtMillis = System.currentTimeMillis(),
            linkStatus = ContactLinkStatus.LINKED
        )
        runCatching { upsertContactUseCase(contact) }
            .onFailure { Log.e(TAG, "Failed to auto-add contact from SMS", it) }
    }

    companion object {
        private const val TAG = "SafeWordSmsReceiver"
        private val safeWordNameRegex = Regex("SafeWord\\s+'([^']+)'")
    }
}
