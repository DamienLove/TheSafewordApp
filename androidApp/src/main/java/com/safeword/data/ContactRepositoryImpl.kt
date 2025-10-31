package com.safeword.data

import android.telephony.PhoneNumberUtils
import com.safeword.data.db.ContactDao
import com.safeword.data.db.ContactEntity
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.model.ContactLinkStatus
import com.safeword.shared.domain.model.PlanTier
import com.safeword.shared.domain.repository.ContactRepository
import com.safeword.util.PhoneCanonicalizer
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
        val canonicalPhone = PhoneCanonicalizer.canonicalize(contact.phone) ?: contact.phone
        val entity = contact.copy(phone = canonicalPhone).toEntity()
        val id = dao.upsert(entity)
        return contact.copy(
            id = if (id == 0L) contact.id else id,
            phone = canonicalPhone
        )
    }

    override suspend fun delete(contactId: Long) {
        dao.delete(contactId)
    }

    override suspend fun getContact(contactId: Long): Contact? =
        dao.getById(contactId)?.toDomain()

    override suspend fun findByPhone(phone: String): Contact? {
        val candidates = linkedSetOf<String>()
        PhoneCanonicalizer.canonicalize(phone)
            ?.takeIf { it.isNotBlank() }
            ?.let { candidates.add(it) }

        PhoneNumberUtils.normalizeNumber(phone)
            ?.takeIf { it.isNotBlank() }
            ?.let { normalized ->
                if (!normalized.startsWith("+") && phone.startsWith("+")) {
                    candidates.add("+$normalized")
                }
                candidates.add(normalized)
            }

        phone.trim()
            .takeIf { it.isNotEmpty() }
            ?.let { candidates.add(it) }

        candidates.forEach { candidate ->
            dao.getByPhone(candidate)?.toDomain()?.let { return it }
        }

        val fallback = dao.getAll().firstOrNull { entity ->
            PhoneCanonicalizer.numbersMatch(entity.phone, phone)
        }
        return fallback?.toDomain()
    }

    override suspend fun listContacts(): List<Contact> =
        dao.getAll().map { it.toDomain() }

    private fun ContactEntity.toDomain(): Contact {
        val parsedStatus = runCatching { ContactLinkStatus.valueOf(linkStatus) }.getOrNull()
        val status = when {
            parsedStatus != null -> parsedStatus
            safewordPeer -> ContactLinkStatus.LINKED
            else -> ContactLinkStatus.UNLINKED
        }
        val canonicalPhone = PhoneCanonicalizer.canonicalize(phone) ?: phone
        return Contact(
            id = id,
            name = name,
            phone = canonicalPhone,
            email = email,
            createdAtMillis = createdAt,
            linkStatus = status,
            planTier = planTier?.let { runCatching { PlanTier.valueOf(it) }.getOrNull() }
        )
    }

    private fun Contact.toEntity(): ContactEntity = ContactEntity(
        id = id ?: 0,
        name = name,
        phone = phone,
        email = email,
        createdAt = createdAtMillis,
        safewordPeer = linkStatus == ContactLinkStatus.LINKED,
        planTier = planTier?.name,
        linkStatus = linkStatus.name
    )
}


