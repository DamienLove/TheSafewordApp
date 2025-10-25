package com.safeword.ui.contacts;

import com.safeword.shared.domain.repository.ContactRepository;
import com.safeword.shared.domain.usecase.DeleteContactUseCase;
import com.safeword.shared.domain.usecase.UpsertContactUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ContactsViewModel_Factory implements Factory<ContactsViewModel> {
  private final Provider<ContactRepository> contactRepositoryProvider;

  private final Provider<UpsertContactUseCase> upsertContactProvider;

  private final Provider<DeleteContactUseCase> deleteContactProvider;

  public ContactsViewModel_Factory(Provider<ContactRepository> contactRepositoryProvider,
      Provider<UpsertContactUseCase> upsertContactProvider,
      Provider<DeleteContactUseCase> deleteContactProvider) {
    this.contactRepositoryProvider = contactRepositoryProvider;
    this.upsertContactProvider = upsertContactProvider;
    this.deleteContactProvider = deleteContactProvider;
  }

  @Override
  public ContactsViewModel get() {
    return newInstance(contactRepositoryProvider.get(), upsertContactProvider.get(), deleteContactProvider.get());
  }

  public static ContactsViewModel_Factory create(
      Provider<ContactRepository> contactRepositoryProvider,
      Provider<UpsertContactUseCase> upsertContactProvider,
      Provider<DeleteContactUseCase> deleteContactProvider) {
    return new ContactsViewModel_Factory(contactRepositoryProvider, upsertContactProvider, deleteContactProvider);
  }

  public static ContactsViewModel newInstance(ContactRepository contactRepository,
      UpsertContactUseCase upsertContact, DeleteContactUseCase deleteContact) {
    return new ContactsViewModel(contactRepository, upsertContact, deleteContact);
  }
}
