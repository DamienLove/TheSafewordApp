package com.safeword.di;

import com.safeword.shared.domain.repository.SettingsGateway;
import com.safeword.shared.domain.usecase.ToggleIncomingModeUseCase;
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
public final class DomainModule_ProvideToggleIncomingModeUseCaseFactory implements Factory<ToggleIncomingModeUseCase> {
  private final Provider<SettingsGateway> settingsGatewayProvider;

  public DomainModule_ProvideToggleIncomingModeUseCaseFactory(
      Provider<SettingsGateway> settingsGatewayProvider) {
    this.settingsGatewayProvider = settingsGatewayProvider;
  }

  @Override
  public ToggleIncomingModeUseCase get() {
    return provideToggleIncomingModeUseCase(settingsGatewayProvider.get());
  }

  public static DomainModule_ProvideToggleIncomingModeUseCaseFactory create(
      Provider<SettingsGateway> settingsGatewayProvider) {
    return new DomainModule_ProvideToggleIncomingModeUseCaseFactory(settingsGatewayProvider);
  }

  public static ToggleIncomingModeUseCase provideToggleIncomingModeUseCase(
      SettingsGateway settingsGateway) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideToggleIncomingModeUseCase(settingsGateway));
  }
}
