package com.safeword.data

import com.safeword.data.db.ContactDao
import com.safeword.data.db.ContactEntity
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ContactRepositoryImpl(
    private val dao: ContactDao
) : ContactRepository {

    override fun observeContacts(): Flow<List<Contact>> =
        dao.observeContacts().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun upsert(contact: Contact): Contact {
        val entity = contact.toEntity()
        val id = dao.upsert(entity)
        return contact.copy(id = if (id == 0L) contact.id else id)
    }

    override suspend fun delete(contactId: Long) {
        dao.delete(contactId)
    }

    override suspend fun getContact(contactId: Long): Contact? =
        dao.getById(contactId)?.toDomain()

    private fun ContactEntity.toDomain(): Contact = Contact(
        id = id,
        name = name,
        phone = phone,
        email = email,
        createdAtMillis = createdAt
    )

    private fun Contact.toEntity(): ContactEntity = ContactEntity(
        id = id ?: 0,
        name = name,
        phone = phone,
        email = email,
        createdAt = createdAtMillis
    )
}

