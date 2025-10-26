package com.safeword.di;

import com.safeword.shared.domain.repository.ContactRepository;
import com.safeword.shared.domain.usecase.DeleteContactUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DomainModule_ProvideDeleteContactUseCaseFactory implements Factory<DeleteContactUseCase> {
  private final Provider<ContactRepository> contactRepositoryProvider;

  public DomainModule_ProvideDeleteContactUseCaseFactory(
      Provider<ContactRepository> contactRepositoryProvider) {
    this.contactRepositoryProvider = contactRepositoryProvider;
  }

  @Override
  public DeleteContactUseCase get() {
    return provideDeleteContactUseCase(contactRepositoryProvider.get());
  }

  public static DomainModule_ProvideDeleteContactUseCaseFactory create(
      Provider<ContactRepository> contactRepositoryProvider) {
    return new DomainModule_ProvideDeleteContactUseCaseFactory(contactRepositoryProvider);
  }

  public static DeleteContactUseCase provideDeleteContactUseCase(
      ContactRepository contactRepository) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideDeleteContactUseCase(contactRepository));
  }
}
