package com.safeword.di;

import com.safeword.shared.domain.repository.ContactRepository;
import com.safeword.shared.domain.usecase.UpsertContactUseCase;
import com.safeword.shared.util.TimeProvider;
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
public final class DomainModule_ProvideUpsertContactUseCaseFactory implements Factory<UpsertContactUseCase> {
  private final Provider<ContactRepository> contactRepositoryProvider;

  private final Provider<TimeProvider> timeProvider;

  public DomainModule_ProvideUpsertContactUseCaseFactory(
      Provider<ContactRepository> contactRepositoryProvider, Provider<TimeProvider> timeProvider) {
    this.contactRepositoryProvider = contactRepositoryProvider;
    this.timeProvider = timeProvider;
  }

  @Override
  public UpsertContactUseCase get() {
    return provideUpsertContactUseCase(contactRepositoryProvider.get(), timeProvider.get());
  }

  public static DomainModule_ProvideUpsertContactUseCaseFactory create(
      Provider<ContactRepository> contactRepositoryProvider, Provider<TimeProvider> timeProvider) {
    return new DomainModule_ProvideUpsertContactUseCaseFactory(contactRepositoryProvider, timeProvider);
  }

  public static UpsertContactUseCase provideUpsertContactUseCase(
      ContactRepository contactRepository, TimeProvider timeProvider) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideUpsertContactUseCase(contactRepository, timeProvider));
  }
}
