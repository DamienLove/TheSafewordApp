package com.safeword.di;

import com.safeword.shared.domain.repository.SettingsGateway;
import com.safeword.shared.domain.usecase.TogglePlaySirenUseCase;
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
public final class DomainModule_ProvideTogglePlaySirenUseCaseFactory implements Factory<TogglePlaySirenUseCase> {
  private final Provider<SettingsGateway> settingsGatewayProvider;

  public DomainModule_ProvideTogglePlaySirenUseCaseFactory(
      Provider<SettingsGateway> settingsGatewayProvider) {
    this.settingsGatewayProvider = settingsGatewayProvider;
  }

  @Override
  public TogglePlaySirenUseCase get() {
    return provideTogglePlaySirenUseCase(settingsGatewayProvider.get());
  }

  public static DomainModule_ProvideTogglePlaySirenUseCaseFactory create(
      Provider<SettingsGateway> settingsGatewayProvider) {
    return new DomainModule_ProvideTogglePlaySirenUseCaseFactory(settingsGatewayProvider);
  }

  public static TogglePlaySirenUseCase provideTogglePlaySirenUseCase(
      SettingsGateway settingsGateway) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideTogglePlaySirenUseCase(settingsGateway));
  }
}
