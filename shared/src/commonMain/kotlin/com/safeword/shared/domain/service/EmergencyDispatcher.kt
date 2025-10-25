package com.safeword.shared.domain.service

import com.safeword.shared.domain.model.AlertEvent
import com.safeword.shared.domain.model.Contact

interface EmergencyDispatcher {
    suspend fun raiseRinger()
    suspend fun playSiren(enabled: Boolean)
    suspend fun resolveLocation(): Pair<Double, Double>?
    suspend fun sendSms(message: String, contacts: List<Contact>): Int
    suspend fun showEmergencyPrompt(detectedWord: String)
    suspend fun logEvent(event: AlertEvent)
}

