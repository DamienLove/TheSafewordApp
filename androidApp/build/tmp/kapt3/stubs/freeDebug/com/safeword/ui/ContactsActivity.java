package com.safeword.ui;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0012\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0018H\u0002J\u0010\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0018\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\fH\u0002J\u0010\u0010!\u001a\u00020\u00162\u0006\u0010\"\u001a\u00020#H\u0002J\u0010\u0010$\u001a\u00020\u00162\u0006\u0010\u0005\u001a\u00020\fH\u0002J\u0010\u0010%\u001a\u00020\u00162\u0006\u0010 \u001a\u00020\fH\u0002J\u0010\u0010&\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\b\u0010\'\u001a\u00020\u0016H\u0002J\u0012\u0010(\u001a\u00020\u00162\b\u0010)\u001a\u0004\u0018\u00010*H\u0014J\u0010\u0010+\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020-H\u0016J\u0012\u0010.\u001a\u00020\u00162\b\u0010\u001b\u001a\u0004\u0018\u00010\u0018H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\f\u0012\n \n*\u0004\u0018\u00010\t0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006/"}, d2 = {"Lcom/safeword/ui/ContactsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapter", "Lcom/safeword/ui/contacts/ContactsAdapter;", "binding", "Lcom/safeword/databinding/ActivityContactsBinding;", "contactsPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "kotlin.jvm.PlatformType", "pendingDialogBinding", "Lcom/safeword/databinding/DialogContactBinding;", "pickContactLauncher", "Ljava/lang/Void;", "viewModel", "Lcom/safeword/ui/contacts/ContactsViewModel;", "getViewModel", "()Lcom/safeword/ui/contacts/ContactsViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "callContact", "", "contact", "Lcom/safeword/shared/domain/model/Contact;", "canAddAnotherContact", "", "existing", "deleteContact", "importContact", "uri", "Landroid/net/Uri;", "dialogBinding", "launchContactIntent", "intent", "Landroid/content/Intent;", "launchContactPicker", "launchContactPickerWithPermission", "messageContact", "observeContacts", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "showContactDialog", "androidApp_freeDebug"})
public final class ContactsActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.safeword.databinding.ActivityContactsBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.safeword.ui.contacts.ContactsAdapter adapter;
    @org.jetbrains.annotations.Nullable()
    private com.safeword.databinding.DialogContactBinding pendingDialogBinding;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.Void> pickContactLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> contactsPermissionLauncher = null;
    
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
    
    private final boolean canAddAnotherContact(com.safeword.shared.domain.model.Contact existing) {
        return false;
    }
    
    private final void launchContactPickerWithPermission(com.safeword.databinding.DialogContactBinding dialogBinding) {
    }
    
    private final void launchContactPicker(com.safeword.databinding.DialogContactBinding binding) {
    }
    
    private final void importContact(android.net.Uri uri, com.safeword.databinding.DialogContactBinding dialogBinding) {
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