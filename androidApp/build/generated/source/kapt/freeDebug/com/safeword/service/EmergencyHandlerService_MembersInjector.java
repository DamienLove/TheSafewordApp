package com.safeword.service;

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
public final class EmergencyHandlerService_MembersInjector implements MembersInjector<EmergencyHandlerService> {
  private final Provider<SafeWordEngine> engineProvider;

  public EmergencyHandlerService_MembersInjector(Provider<SafeWordEngine> engineProvider) {
    this.engineProvider = engineProvider;
  }

  public static MembersInjector<EmergencyHandlerService> create(
      Provider<SafeWordEngine> engineProvider) {
    return new EmergencyHandlerService_MembersInjector(engineProvider);
  }

  @Override
  public void injectMembers(EmergencyHandlerService instance) {
    injectEngine(instance, engineProvider.get());
  }

  @InjectedFieldSignature("com.safeword.service.EmergencyHandlerService.engine")
  public static void injectEngine(EmergencyHandlerService instance, SafeWordEngine engine) {
    instance.engine = engine;
  }
}
