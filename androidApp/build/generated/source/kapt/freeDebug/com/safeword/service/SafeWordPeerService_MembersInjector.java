package com.safeword.service;

import com.safeword.shared.bridge.PeerBridge;
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
public final class SafeWordPeerService_MembersInjector implements MembersInjector<SafeWordPeerService> {
  private final Provider<PeerBridge> peerBridgeProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  public SafeWordPeerService_MembersInjector(Provider<PeerBridge> peerBridgeProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.peerBridgeProvider = peerBridgeProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  public static MembersInjector<SafeWordPeerService> create(Provider<PeerBridge> peerBridgeProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new SafeWordPeerService_MembersInjector(peerBridgeProvider, notificationHelperProvider);
  }

  @Override
  public void injectMembers(SafeWordPeerService instance) {
    injectPeerBridge(instance, peerBridgeProvider.get());
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  @InjectedFieldSignature("com.safeword.service.SafeWordPeerService.peerBridge")
  public static void injectPeerBridge(SafeWordPeerService instance, PeerBridge peerBridge) {
    instance.peerBridge = peerBridge;
  }

  @InjectedFieldSignature("com.safeword.service.SafeWordPeerService.notificationHelper")
  public static void injectNotificationHelper(SafeWordPeerService instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
