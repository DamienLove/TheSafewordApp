package com.safeword.ui.contacts;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\fR\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/safeword/ui/contacts/ContactsViewModel;", "Landroidx/lifecycle/ViewModel;", "contactRepository", "Lcom/safeword/shared/domain/repository/ContactRepository;", "upsertContact", "Lcom/safeword/shared/domain/usecase/UpsertContactUseCase;", "deleteContact", "Lcom/safeword/shared/domain/usecase/DeleteContactUseCase;", "(Lcom/safeword/shared/domain/repository/ContactRepository;Lcom/safeword/shared/domain/usecase/UpsertContactUseCase;Lcom/safeword/shared/domain/usecase/DeleteContactUseCase;)V", "contacts", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/safeword/shared/domain/model/Contact;", "getContacts", "()Lkotlinx/coroutines/flow/StateFlow;", "delete", "", "contactId", "", "save", "contact", "androidApp_freeDebug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ContactsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.usecase.UpsertContactUseCase upsertContact = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.usecase.DeleteContactUseCase deleteContact = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.safeword.shared.domain.model.Contact>> contacts = null;
    
    @javax.inject.Inject()
    public ContactsViewModel(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.ContactRepository contactRepository, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.usecase.UpsertContactUseCase upsertContact, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.usecase.DeleteContactUseCase deleteContact) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.safeword.shared.domain.model.Contact>> getContacts() {
        return null;
    }
    
    public final void save(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.model.Contact contact) {
    }
    
    public final void delete(long contactId) {
    }
}