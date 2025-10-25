package com.safeword.shared.domain.usecase

import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.repository.ContactRepository
import com.safeword.shared.util.TimeProvider

class UpsertContactUseCase(
    private val contactRepository: ContactRepository,
    private val timeProvider: TimeProvider
) {
    suspend operator fun invoke(contact: Contact): Contact {
        val trimmed = contact.copy(
            name = contact.name.trim(),
            phone = contact.phone.trim(),
            email = contact.email?.trim(),
            createdAtMillis = contact.createdAtMillis.takeIf { it > 0 } ?: timeProvider.nowMillis()
        )
        require(trimmed.name.isNotBlank()) { "Contact name required" }
        require(trimmed.phone.isNotBlank()) { "Contact phone required" }
        return contactRepository.upsert(trimmed)
    }
}

class DeleteContactUseCase(
    private val contactRepository: ContactRepository
) {
    suspend operator fun invoke(contactId: Long) {
        contactRepository.delete(contactId)
    }
}

