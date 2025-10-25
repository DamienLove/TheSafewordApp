package com.safeword.ui;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u0010\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0016\u001a\u00020\u000eH\u0002J\u0012\u0010\u0017\u001a\u00020\u000e2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0012\u0010\u001e\u001a\u00020\u000e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0010H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n\u00a8\u0006 "}, d2 = {"Lcom/safeword/ui/ContactsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapter", "Lcom/safeword/ui/contacts/ContactsAdapter;", "binding", "Lcom/safeword/databinding/ActivityContactsBinding;", "viewModel", "Lcom/safeword/ui/contacts/ContactsViewModel;", "getViewModel", "()Lcom/safeword/ui/contacts/ContactsViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "callContact", "", "contact", "Lcom/safeword/shared/domain/model/Contact;", "deleteContact", "launchContactIntent", "intent", "Landroid/content/Intent;", "messageContact", "observeContacts", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "showContactDialog", "existing", "androidApp_debug"})
public final class ContactsActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.safeword.databinding.ActivityContactsBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.safeword.ui.contacts.ContactsAdapter adapter;
    
    public ContactsActivity() {
        super();
    }
    
    private final com.safeword.ui.contacts.ContactsViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void observeContacts() {
    }
    
    private final void showContactDialog(com.safeword.shared.domain.model.Contact existing) {
    }
    
    private final void deleteContact(com.safeword.shared.domain.model.Contact contact) {
    }
    
    private final void callContact(com.safeword.shared.domain.model.Contact contact) {
    }
    
    private final void messageContact(com.safeword.shared.domain.model.Contact contact) {
    }
    
    private final void launchContactIntent(android.content.Intent intent) {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
}