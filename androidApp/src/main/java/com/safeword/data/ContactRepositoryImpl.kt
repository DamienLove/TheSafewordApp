package com.safeword.data

import com.safeword.data.db.ContactDao
import com.safeword.data.db.ContactEntity
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.model.PlanTier
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

    override suspend fun findByPhone(phone: String): Contact? =
        dao.getByPhone(phone)?.toDomain()

    override suspend fun listContacts(): List<Contact> =
        dao.getAll().map { it.toDomain() }

    private fun ContactEntity.toDomain(): Contact = Contact(
        id = id,
        name = name,
        phone = phone,
        email = email,
        createdAtMillis = createdAt,
        isSafewordPeer = safewordPeer,
        planTier = planTier?.let { runCatching { PlanTier.valueOf(it) }.getOrNull() }
    )

    private fun Contact.toEntity(): ContactEntity = ContactEntity(
        id = id ?: 0,
        name = name,
        phone = phone,
        email = email,
        createdAt = createdAtMillis,
        safewordPeer = isSafewordPeer,
        planTier = planTier?.name
    )
}

