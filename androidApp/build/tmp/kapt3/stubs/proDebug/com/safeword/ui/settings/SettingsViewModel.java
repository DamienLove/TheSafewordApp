package com.safeword.ui.settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B?\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0010J\u0006\u0010\u0016\u001a\u00020\u0017J\u0016\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aJ\u000e\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\u00172\u0006\u0010 \u001a\u00020\u001eJ\u000e\u0010!\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020#J\u000e\u0010$\u001a\u00020\u00172\u0006\u0010%\u001a\u00020\u001eR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/safeword/ui/settings/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "settingsGateway", "Lcom/safeword/shared/domain/repository/SettingsGateway;", "updateSafeWords", "Lcom/safeword/shared/domain/usecase/UpdateSafeWordsUseCase;", "updateSensitivity", "Lcom/safeword/shared/domain/usecase/UpdateSensitivityUseCase;", "toggleIncludeLocation", "Lcom/safeword/shared/domain/usecase/ToggleIncludeLocationUseCase;", "togglePlaySiren", "Lcom/safeword/shared/domain/usecase/TogglePlaySirenUseCase;", "toggleTestMode", "Lcom/safeword/shared/domain/usecase/ToggleTestModeUseCase;", "engine", "Lcom/safeword/shared/domain/SafeWordEngine;", "(Lcom/safeword/shared/domain/repository/SettingsGateway;Lcom/safeword/shared/domain/usecase/UpdateSafeWordsUseCase;Lcom/safeword/shared/domain/usecase/UpdateSensitivityUseCase;Lcom/safeword/shared/domain/usecase/ToggleIncludeLocationUseCase;Lcom/safeword/shared/domain/usecase/TogglePlaySirenUseCase;Lcom/safeword/shared/domain/usecase/ToggleTestModeUseCase;Lcom/safeword/shared/domain/SafeWordEngine;)V", "settings", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/safeword/shared/domain/model/SafeWordSettings;", "getSettings", "()Lkotlinx/coroutines/flow/StateFlow;", "runTest", "", "saveSafeWords", "first", "", "second", "setIncludeLocation", "include", "", "setPlaySiren", "play", "setSensitivity", "value", "", "setTestMode", "enabled", "androidApp_proDebug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.usecase.UpdateSafeWordsUseCase updateSafeWords = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.usecase.UpdateSensitivityUseCase updateSensitivity = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.usecase.ToggleIncludeLocationUseCase toggleIncludeLocation = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.usecase.TogglePlaySirenUseCase togglePlaySiren = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.usecase.ToggleTestModeUseCase toggleTestMode = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.SafeWordEngine engine = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.safeword.shared.domain.model.SafeWordSettings> settings = null;
    
    @javax.inject.Inject()
    public SettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.SettingsGateway settingsGateway, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.usecase.UpdateSafeWordsUseCase updateSafeWords, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.usecase.UpdateSensitivityUseCase updateSensitivity, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.usecase.ToggleIncludeLocationUseCase toggleIncludeLocation, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.usecase.TogglePlaySirenUseCase togglePlaySiren, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.usecase.ToggleTestModeUseCase toggleTestMode, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.SafeWordEngine engine) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.safeword.shared.domain.model.SafeWordSettings> getSettings() {
        return null;
    }
    
    public final void saveSafeWords(@org.jetbrains.annotations.NotNull()
    java.lang.String first, @org.jetbrains.annotations.NotNull()
    java.lang.String second) {
    }
    
    public final void setSensitivity(int value) {
    }
    
    public final void setIncludeLocation(boolean include) {
    }
    
    public final void setPlaySiren(boolean play) {
    }
    
    public final void setTestMode(boolean enabled) {
    }
    
    public final void runTest() {
    }
}