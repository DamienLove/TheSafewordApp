package com.safeword.di;

import android.content.Context;
import com.safeword.shared.bridge.PeerBridge;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DomainModule_ProvidePeerBridgeFactory implements Factory<PeerBridge> {
  private final Provider<Context> contextProvider;

  private final Provider<CoroutineScope> scopeProvider;

  public DomainModule_ProvidePeerBridgeFactory(Provider<Context> contextProvider,
      Provider<CoroutineScope> scopeProvider) {
    this.contextProvider = contextProvider;
    this.scopeProvider = scopeProvider;
  }

  @Override
  public PeerBridge get() {
    return providePeerBridge(contextProvider.get(), scopeProvider.get());
  }

  public static DomainModule_ProvidePeerBridgeFactory create(Provider<Context> contextProvider,
      Provider<CoroutineScope> scopeProvider) {
    return new DomainModule_ProvidePeerBridgeFactory(contextProvider, scopeProvider);
  }

  public static PeerBridge providePeerBridge(Context context, CoroutineScope scope) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.providePeerBridge(context, scope));
  }
}
