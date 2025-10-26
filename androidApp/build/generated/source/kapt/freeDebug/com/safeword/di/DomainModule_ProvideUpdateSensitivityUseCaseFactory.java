package com.safeword.di;

import com.safeword.shared.domain.repository.SettingsGateway;
import com.safeword.shared.domain.usecase.UpdateSensitivityUseCase;
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
public final class DomainModule_ProvideUpdateSensitivityUseCaseFactory implements Factory<UpdateSensitivityUseCase> {
  private final Provider<SettingsGateway> settingsGatewayProvider;

  public DomainModule_ProvideUpdateSensitivityUseCaseFactory(
      Provider<SettingsGateway> settingsGatewayProvider) {
    this.settingsGatewayProvider = settingsGatewayProvider;
  }

  @Override
  public UpdateSensitivityUseCase get() {
    return provideUpdateSensitivityUseCase(settingsGatewayProvider.get());
  }

  public static DomainModule_ProvideUpdateSensitivityUseCaseFactory create(
      Provider<SettingsGateway> settingsGatewayProvider) {
    return new DomainModule_ProvideUpdateSensitivityUseCaseFactory(settingsGatewayProvider);
  }

  public static UpdateSensitivityUseCase provideUpdateSensitivityUseCase(
      SettingsGateway settingsGateway) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideUpdateSensitivityUseCase(settingsGateway));
  }
}
