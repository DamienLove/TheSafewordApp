package com.safeword.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safeword.shared.domain.SafeWordEngine
import com.safeword.shared.domain.model.Contact
import com.safeword.shared.domain.model.ContactEngagementType
import com.safeword.shared.domain.repository.ContactRepository
import com.safeword.shared.domain.usecase.DeleteContactUseCase
import com.safeword.shared.domain.usecase.UpsertContactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    contactRepository: ContactRepository,
    private val upsertContact: UpsertContactUseCase,
    private val deleteContact: DeleteContactUseCase,
    private val engine: SafeWordEngine
) : ViewModel() {

    val contacts: StateFlow<List<Contact>> = contactRepository.observeContacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun save(contact: Contact) {
        viewModelScope.launch { upsertContact(contact) }
    }

    fun delete(contactId: Long) {
        viewModelScope.launch { deleteContact(contactId) }
    }

    fun ping(contact: Contact, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = engine.sendCheckIn(contact)
            onResult(success)
        }
    }

    fun sendContactSignal(
        contact: Contact,
        type: ContactEngagementType,
        emergency: Boolean,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val sent = engine.sendContactSignal(contact, type, emergency)
            onResult(sent)
        }
    }

    fun sendInvite(contact: Contact, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val sent = engine.sendInvite(contact)
            onResult(sent)
        }
    }
}
