package com.safeword.shared.ios.data

import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

class IosContactRepository(
    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults()
) : ContactRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private val state = MutableStateFlow(load())

    override fun observeContacts(): Flow<List<Contact>> = state.asStateFlow()

    override suspend fun upsert(contact: Contact): Contact {
        val contacts = state.value.toMutableList()
        val updated = if (contact.id == null) {
            val nextId = (contacts.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
            contact.copy(id = nextId)
        } else contact
        val index = contacts.indexOfFirst { it.id == updated.id }
        if (index >= 0) {
            contacts[index] = updated
        } else {
            contacts.add(updated)
        }
        persist(contacts)
        return updated
    }

    override suspend fun delete(contactId: Long) {
        val contacts = state.value.filterNot { it.id == contactId }
        persist(contacts)
    }

    override suspend fun getContact(contactId: Long): Contact? = state.value.firstOrNull { it.id == contactId }

    private fun persist(contacts: List<Contact>) {
        state.value = contacts.sortedBy { it.name.lowercase() }
        userDefaults.setObject(json.encodeToString(state.value), forKey = KEY)
    }

    private fun load(): List<Contact> {
        val stored = userDefaults.stringForKey(KEY) ?: return emptyList()
        return runCatching { json.decodeFromString<List<Contact>>(stored) }.getOrDefault(emptyList())
    }

    companion object {
        private const val KEY = "safeword_contacts"
    }
}
