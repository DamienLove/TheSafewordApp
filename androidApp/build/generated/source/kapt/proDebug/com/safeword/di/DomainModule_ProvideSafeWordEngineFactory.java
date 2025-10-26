package com.safeword.di;

import com.safeword.shared.bridge.PeerBridge;
import com.safeword.shared.domain.SafeWordEngine;
import com.safeword.shared.domain.repository.AlertEventRepository;
import com.safeword.shared.domain.repository.ContactRepository;
import com.safeword.shared.domain.repository.SettingsGateway;
import com.safeword.shared.domain.service.EmergencyDispatcher;
import com.safeword.shared.util.TimeProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DomainModule_ProvideSafeWordEngineFactory implements Factory<SafeWordEngine> {
  private final Provider<SettingsGateway> settingsGatewayProvider;

  private final Provider<ContactRepository> contactRepositoryProvider;

  private final Provider<AlertEventRepository> alertEventRepositoryProvider;

  private final Provider<EmergencyDispatcher> dispatcherProvider;

  private final Provider<PeerBridge> peerBridgeProvider;

  private final Provider<TimeProvider> timeProvider;

  private final Provider<CoroutineScope> scopeProvider;

  public DomainModule_ProvideSafeWordEngineFactory(
      Provider<SettingsGateway> settingsGatewayProvider,
      Provider<ContactRepository> contactRepositoryProvider,
      Provider<AlertEventRepository> alertEventRepositoryProvider,
      Provider<EmergencyDispatcher> dispatcherProvider, Provider<PeerBridge> peerBridgeProvider,
      Provider<TimeProvider> timeProvider, Provider<CoroutineScope> scopeProvider) {
    this.settingsGatewayProvider = settingsGatewayProvider;
    this.contactRepositoryProvider = contactRepositoryProvider;
    this.alertEventRepositoryProvider = alertEventRepositoryProvider;
    this.dispatcherProvider = dispatcherProvider;
    this.peerBridgeProvider = peerBridgeProvider;
    this.timeProvider = timeProvider;
    this.scopeProvider = scopeProvider;
  }

  @Override
  public SafeWordEngine get() {
    return provideSafeWordEngine(settingsGatewayProvider.get(), contactRepositoryProvider.get(), alertEventRepositoryProvider.get(), dispatcherProvider.get(), peerBridgeProvider.get(), timeProvider.get(), scopeProvider.get());
  }

  public static DomainModule_ProvideSafeWordEngineFactory create(
      Provider<SettingsGateway> settingsGatewayProvider,
      Provider<ContactRepository> contactRepositoryProvider,
      Provider<AlertEventRepository> alertEventRepositoryProvider,
      Provider<EmergencyDispatcher> dispatcherProvider, Provider<PeerBridge> peerBridgeProvider,
      Provider<TimeProvider> timeProvider, Provider<CoroutineScope> scopeProvider) {
    return new DomainModule_ProvideSafeWordEngineFactory(settingsGatewayProvider, contactRepositoryProvider, alertEventRepositoryProvider, dispatcherProvider, peerBridgeProvider, timeProvider, scopeProvider);
  }

  public static SafeWordEngine provideSafeWordEngine(SettingsGateway settingsGateway,
      ContactRepository contactRepository, AlertEventRepository alertEventRepository,
      EmergencyDispatcher dispatcher, PeerBridge peerBridge, TimeProvider timeProvider,
      CoroutineScope scope) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideSafeWordEngine(settingsGateway, contactRepository, alertEventRepository, dispatcher, peerBridge, timeProvider, scope));
  }
}
