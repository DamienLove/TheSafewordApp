package com.safeword.util

import android.telephony.PhoneNumberUtils
import java.util.Locale

object PhoneCanonicalizer {
    fun canonicalize(raw: String?): String? {
        if (raw.isNullOrBlank()) return null
        val trimmed = raw.trim()
        val e164 = formatToE164(trimmed)
        if (!e164.isNullOrBlank()) {
            return e164
        }
        val normalized = PhoneNumberUtils.normalizeNumber(trimmed)
        if (normalized.isNotBlank()) {
            return if (trimmed.startsWith("+") && !normalized.startsWith("+")) {
                "+$normalized"
            } else {
                normalized
            }
        }
        val filtered = trimmed.filter { it.isDigit() || it == '+' }
        return filtered.ifBlank { null }
    }

    fun numbersMatch(first: String?, second: String?): Boolean {
        if (first.isNullOrBlank() || second.isNullOrBlank()) return false
        val canonicalFirst = canonicalize(first)
        val canonicalSecond = canonicalize(second)
        if (!canonicalFirst.isNullOrBlank() && canonicalFirst == canonicalSecond) {
            return true
        }
        return PhoneNumberUtils.compare(first, second)
    }

    private fun formatToE164(number: String): String? {
        val countryIso = Locale.getDefault().country.takeIf { it.isNotBlank() }
        return try {
            if (countryIso != null) {
                PhoneNumberUtils.formatNumberToE164(number, countryIso)
            } else {
                null
            }
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}
