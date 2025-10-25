package com.safeword.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safeword.shared.domain.model.Contact
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
    private val deleteContact: DeleteContactUseCase
) : ViewModel() {

    val contacts: StateFlow<List<Contact>> = contactRepository.observeContacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun save(contact: Contact) {
        viewModelScope.launch { upsertContact(contact) }
    }

    fun delete(contactId: Long) {
        viewModelScope.launch { deleteContact(contactId) }
    }
}
