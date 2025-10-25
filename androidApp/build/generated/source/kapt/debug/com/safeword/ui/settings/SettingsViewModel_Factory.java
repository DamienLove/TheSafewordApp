package com.safeword.ui.settings;

import com.safeword.shared.domain.SafeWordEngine;
import com.safeword.shared.domain.repository.SettingsGateway;
import com.safeword.shared.domain.usecase.ToggleIncludeLocationUseCase;
import com.safeword.shared.domain.usecase.TogglePlaySirenUseCase;
import com.safeword.shared.domain.usecase.ToggleTestModeUseCase;
import com.safeword.shared.domain.usecase.UpdateSafeWordsUseCase;
import com.safeword.shared.domain.usecase.UpdateSensitivityUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SettingsGateway> settingsGatewayProvider;

  private final Provider<UpdateSafeWordsUseCase> updateSafeWordsProvider;

  private final Provider<UpdateSensitivityUseCase> updateSensitivityProvider;

  private final Provider<ToggleIncludeLocationUseCase> toggleIncludeLocationProvider;

  private final Provider<TogglePlaySirenUseCase> togglePlaySirenProvider;

  private final Provider<ToggleTestModeUseCase> toggleTestModeProvider;

  private final Provider<SafeWordEngine> engineProvider;

  public SettingsViewModel_Factory(Provider<SettingsGateway> settingsGatewayProvider,
      Provider<UpdateSafeWordsUseCase> updateSafeWordsProvider,
      Provider<UpdateSensitivityUseCase> updateSensitivityProvider,
      Provider<ToggleIncludeLocationUseCase> toggleIncludeLocationProvider,
      Provider<TogglePlaySirenUseCase> togglePlaySirenProvider,
      Provider<ToggleTestModeUseCase> toggleTestModeProvider,
      Provider<SafeWordEngine> engineProvider) {
    this.settingsGatewayProvider = settingsGatewayProvider;
    this.updateSafeWordsProvider = updateSafeWordsProvider;
    this.updateSensitivityProvider = updateSensitivityProvider;
    this.toggleIncludeLocationProvider = toggleIncludeLocationProvider;
    this.togglePlaySirenProvider = togglePlaySirenProvider;
    this.toggleTestModeProvider = toggleTestModeProvider;
    this.engineProvider = engineProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(settingsGatewayProvider.get(), updateSafeWordsProvider.get(), updateSensitivityProvider.get(), toggleIncludeLocationProvider.get(), togglePlaySirenProvider.get(), toggleTestModeProvider.get(), engineProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<SettingsGateway> settingsGatewayProvider,
      Provider<UpdateSafeWordsUseCase> updateSafeWordsProvider,
      Provider<UpdateSensitivityUseCase> updateSensitivityProvider,
      Provider<ToggleIncludeLocationUseCase> toggleIncludeLocationProvider,
      Provider<TogglePlaySirenUseCase> togglePlaySirenProvider,
      Provider<ToggleTestModeUseCase> toggleTestModeProvider,
      Provider<SafeWordEngine> engineProvider) {
    return new SettingsViewModel_Factory(settingsGatewayProvider, updateSafeWordsProvider, updateSensitivityProvider, toggleIncludeLocationProvider, togglePlaySirenProvider, toggleTestModeProvider, engineProvider);
  }

  public static SettingsViewModel newInstance(SettingsGateway settingsGateway,
      UpdateSafeWordsUseCase updateSafeWords, UpdateSensitivityUseCase updateSensitivity,
      ToggleIncludeLocationUseCase toggleIncludeLocation, TogglePlaySirenUseCase togglePlaySiren,
      ToggleTestModeUseCase toggleTestMode, SafeWordEngine engine) {
    return new SettingsViewModel(settingsGateway, updateSafeWords, updateSensitivity, toggleIncludeLocation, togglePlaySiren, toggleTestMode, engine);
  }
}
