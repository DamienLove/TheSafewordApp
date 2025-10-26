package com.safeword.shared.domain.repository

import com.safeword.shared.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun observeContacts(): Flow<List<Contact>>
    suspend fun upsert(contact: Contact): Contact
    suspend fun delete(contactId: Long)
    suspend fun getContact(contactId: Long): Contact?
    suspend fun findByPhone(phone: String): Contact?
    suspend fun listContacts(): List<Contact>
}
