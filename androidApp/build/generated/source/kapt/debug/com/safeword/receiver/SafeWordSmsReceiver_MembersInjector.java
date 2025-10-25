package com.safeword.receiver;

import com.safeword.shared.domain.SafeWordEngine;
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

  public SafeWordSmsReceiver_MembersInjector(Provider<SafeWordEngine> engineProvider) {
    this.engineProvider = engineProvider;
  }

  public static MembersInjector<SafeWordSmsReceiver> create(
      Provider<SafeWordEngine> engineProvider) {
    return new SafeWordSmsReceiver_MembersInjector(engineProvider);
  }

  @Override
  public void injectMembers(SafeWordSmsReceiver instance) {
    injectEngine(instance, engineProvider.get());
  }

  @InjectedFieldSignature("com.safeword.receiver.SafeWordSmsReceiver.engine")
  public static void injectEngine(SafeWordSmsReceiver instance, SafeWordEngine engine) {
    instance.engine = engine;
  }
}
