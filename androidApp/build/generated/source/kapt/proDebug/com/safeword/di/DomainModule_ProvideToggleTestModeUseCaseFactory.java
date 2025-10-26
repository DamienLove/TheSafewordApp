package com.safeword.di;

import com.safeword.shared.domain.repository.SettingsGateway;
import com.safeword.shared.domain.usecase.ToggleTestModeUseCase;
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
public final class DomainModule_ProvideToggleTestModeUseCaseFactory implements Factory<ToggleTestModeUseCase> {
  private final Provider<SettingsGateway> settingsGatewayProvider;

  public DomainModule_ProvideToggleTestModeUseCaseFactory(
      Provider<SettingsGateway> settingsGatewayProvider) {
    this.settingsGatewayProvider = settingsGatewayProvider;
  }

  @Override
  public ToggleTestModeUseCase get() {
    return provideToggleTestModeUseCase(settingsGatewayProvider.get());
  }

  public static DomainModule_ProvideToggleTestModeUseCaseFactory create(
      Provider<SettingsGateway> settingsGatewayProvider) {
    return new DomainModule_ProvideToggleTestModeUseCaseFactory(settingsGatewayProvider);
  }

  public static ToggleTestModeUseCase provideToggleTestModeUseCase(
      SettingsGateway settingsGateway) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideToggleTestModeUseCase(settingsGateway));
  }
}
