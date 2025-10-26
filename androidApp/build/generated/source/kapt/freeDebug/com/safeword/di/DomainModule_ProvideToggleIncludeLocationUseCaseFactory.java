package com.safeword.di;

import com.safeword.shared.domain.repository.SettingsGateway;
import com.safeword.shared.domain.usecase.ToggleIncludeLocationUseCase;
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
public final class DomainModule_ProvideToggleIncludeLocationUseCaseFactory implements Factory<ToggleIncludeLocationUseCase> {
  private final Provider<SettingsGateway> settingsGatewayProvider;

  public DomainModule_ProvideToggleIncludeLocationUseCaseFactory(
      Provider<SettingsGateway> settingsGatewayProvider) {
    this.settingsGatewayProvider = settingsGatewayProvider;
  }

  @Override
  public ToggleIncludeLocationUseCase get() {
    return provideToggleIncludeLocationUseCase(settingsGatewayProvider.get());
  }

  public static DomainModule_ProvideToggleIncludeLocationUseCaseFactory create(
      Provider<SettingsGateway> settingsGatewayProvider) {
    return new DomainModule_ProvideToggleIncludeLocationUseCaseFactory(settingsGatewayProvider);
  }

  public static ToggleIncludeLocationUseCase provideToggleIncludeLocationUseCase(
      SettingsGateway settingsGateway) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideToggleIncludeLocationUseCase(settingsGateway));
  }
}
