package com.safeword.service;

import com.safeword.shared.domain.SafeWordEngine;
import com.safeword.util.NotificationHelper;
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
public final class VoiceRecognitionService_MembersInjector implements MembersInjector<VoiceRecognitionService> {
  private final Provider<SafeWordEngine> engineProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  public VoiceRecognitionService_MembersInjector(Provider<SafeWordEngine> engineProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.engineProvider = engineProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  public static MembersInjector<VoiceRecognitionService> create(
      Provider<SafeWordEngine> engineProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new VoiceRecognitionService_MembersInjector(engineProvider, notificationHelperProvider);
  }

  @Override
  public void injectMembers(VoiceRecognitionService instance) {
    injectEngine(instance, engineProvider.get());
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  @InjectedFieldSignature("com.safeword.service.VoiceRecognitionService.engine")
  public static void injectEngine(VoiceRecognitionService instance, SafeWordEngine engine) {
    instance.engine = engine;
  }

  @InjectedFieldSignature("com.safeword.service.VoiceRecognitionService.notificationHelper")
  public static void injectNotificationHelper(VoiceRecognitionService instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
