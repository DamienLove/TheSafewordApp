package com.safeword.ui.main;

import com.safeword.shared.domain.SafeWordEngine;
import com.safeword.shared.domain.usecase.ToggleIncomingModeUseCase;
import com.safeword.shared.domain.usecase.ToggleListeningUseCase;
import com.safeword.shared.domain.usecase.ToggleTestModeUseCase;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<SafeWordEngine> engineProvider;

  private final Provider<ToggleListeningUseCase> toggleListeningProvider;

  private final Provider<ToggleIncomingModeUseCase> toggleIncomingModeProvider;

  private final Provider<ToggleTestModeUseCase> toggleTestModeProvider;

  public MainViewModel_Factory(Provider<SafeWordEngine> engineProvider,
      Provider<ToggleListeningUseCase> toggleListeningProvider,
      Provider<ToggleIncomingModeUseCase> toggleIncomingModeProvider,
      Provider<ToggleTestModeUseCase> toggleTestModeProvider) {
    this.engineProvider = engineProvider;
    this.toggleListeningProvider = toggleListeningProvider;
    this.toggleIncomingModeProvider = toggleIncomingModeProvider;
    this.toggleTestModeProvider = toggleTestModeProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(engineProvider.get(), toggleListeningProvider.get(), toggleIncomingModeProvider.get(), toggleTestModeProvider.get());
  }

  public static MainViewModel_Factory create(Provider<SafeWordEngine> engineProvider,
      Provider<ToggleListeningUseCase> toggleListeningProvider,
      Provider<ToggleIncomingModeUseCase> toggleIncomingModeProvider,
      Provider<ToggleTestModeUseCase> toggleTestModeProvider) {
    return new MainViewModel_Factory(engineProvider, toggleListeningProvider, toggleIncomingModeProvider, toggleTestModeProvider);
  }

  public static MainViewModel newInstance(SafeWordEngine engine,
      ToggleListeningUseCase toggleListening, ToggleIncomingModeUseCase toggleIncomingMode,
      ToggleTestModeUseCase toggleTestMode) {
    return new MainViewModel(engine, toggleListening, toggleIncomingMode, toggleTestMode);
  }
}
