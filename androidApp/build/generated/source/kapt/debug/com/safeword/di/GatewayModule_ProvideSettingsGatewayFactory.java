package com.safeword.di;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import com.safeword.shared.domain.repository.SettingsGateway;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class GatewayModule_ProvideSettingsGatewayFactory implements Factory<SettingsGateway> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public GatewayModule_ProvideSettingsGatewayFactory(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public SettingsGateway get() {
    return provideSettingsGateway(dataStoreProvider.get());
  }

  public static GatewayModule_ProvideSettingsGatewayFactory create(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new GatewayModule_ProvideSettingsGatewayFactory(dataStoreProvider);
  }

  public static SettingsGateway provideSettingsGateway(DataStore<Preferences> dataStore) {
    return Preconditions.checkNotNullFromProvides(GatewayModule.INSTANCE.provideSettingsGateway(dataStore));
  }
}
