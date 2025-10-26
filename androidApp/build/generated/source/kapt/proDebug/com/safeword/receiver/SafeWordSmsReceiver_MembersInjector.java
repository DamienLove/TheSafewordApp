package com.safeword.receiver;

import com.safeword.shared.domain.SafeWordEngine;
import com.safeword.shared.domain.repository.ContactRepository;
import com.safeword.shared.domain.usecase.UpsertContactUseCase;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class SafeWordSmsReceiver_MembersInjector implements MembersInjector<SafeWordSmsReceiver> {
  private final Provider<SafeWordEngine> engineProvider;

  private final Provider<UpsertContactUseCase> upsertContactUseCaseProvider;

  private final Provider<ContactRepository> contactRepositoryProvider;

  public SafeWordSmsReceiver_MembersInjector(Provider<SafeWordEngine> engineProvider,
      Provider<UpsertContactUseCase> upsertContactUseCaseProvider,
      Provider<ContactRepository> contactRepositoryProvider) {
    this.engineProvider = engineProvider;
    this.upsertContactUseCaseProvider = upsertContactUseCaseProvider;
    this.contactRepositoryProvider = contactRepositoryProvider;
  }

  public static MembersInjector<SafeWordSmsReceiver> create(Provider<SafeWordEngine> engineProvider,
      Provider<UpsertContactUseCase> upsertContactUseCaseProvider,
      Provider<ContactRepository> contactRepositoryProvider) {
    return new SafeWordSmsReceiver_MembersInjector(engineProvider, upsertContactUseCaseProvider, contactRepositoryProvider);
  }

  @Override
  public void injectMembers(SafeWordSmsReceiver instance) {
    injectEngine(instance, engineProvider.get());
    injectUpsertContactUseCase(instance, upsertContactUseCaseProvider.get());
    injectContactRepository(instance, contactRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.safeword.receiver.SafeWordSmsReceiver.engine")
  public static void injectEngine(SafeWordSmsReceiver instance, SafeWordEngine engine) {
    instance.engine = engine;
  }

  @InjectedFieldSignature("com.safeword.receiver.SafeWordSmsReceiver.upsertContactUseCase")
  public static void injectUpsertContactUseCase(SafeWordSmsReceiver instance,
      UpsertContactUseCase upsertContactUseCase) {
    instance.upsertContactUseCase = upsertContactUseCase;
  }

  @InjectedFieldSignature("com.safeword.receiver.SafeWordSmsReceiver.contactRepository")
  public static void injectContactRepository(SafeWordSmsReceiver instance,
      ContactRepository contactRepository) {
    instance.contactRepository = contactRepository;
  }
}
