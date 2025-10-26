package com.safeword.ui.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0013J\u000e\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0017"}, d2 = {"Lcom/safeword/ui/main/MainViewModel;", "Landroidx/lifecycle/ViewModel;", "engine", "Lcom/safeword/shared/domain/SafeWordEngine;", "toggleListening", "Lcom/safeword/shared/domain/usecase/ToggleListeningUseCase;", "toggleIncomingMode", "Lcom/safeword/shared/domain/usecase/ToggleIncomingModeUseCase;", "toggleTestMode", "Lcom/safeword/shared/domain/usecase/ToggleTestModeUseCase;", "(Lcom/safeword/shared/domain/SafeWordEngine;Lcom/safeword/shared/domain/usecase/ToggleListeningUseCase;Lcom/safeword/shared/domain/usecase/ToggleIncomingModeUseCase;Lcom/safeword/shared/domain/usecase/ToggleTestModeUseCase;)V", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/safeword/ui/main/MainUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "setListening", "", "enabled", "", "setModeIncoming", "incoming", "setTestMode", "androidApp_freeDebug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MainViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.SafeWordEngine engine = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.usecase.ToggleListeningUseCase toggleListening = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.usecase.ToggleIncomingModeUseCase toggleIncomingMode = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.shared.domain.usecase.ToggleTestModeUseCase toggleTestMode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.safeword.ui.main.MainUiState> uiState = null;
    
    @javax.inject.Inject()
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.SafeWordEngine engine, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.usecase.ToggleListeningUseCase toggleListening, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.usecase.ToggleIncomingModeUseCase toggleIncomingMode, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.usecase.ToggleTestModeUseCase toggleTestMode) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.safeword.ui.main.MainUiState> getUiState() {
        return null;
    }
    
    public final void setListening(boolean enabled) {
    }
    
    public final void setModeIncoming(boolean incoming) {
    }
    
    public final void setTestMode(boolean enabled) {
    }
}