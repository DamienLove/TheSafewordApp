package com.safeword.ui;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0014H\u0002J\b\u0010\u0016\u001a\u00020\u0014H\u0002J\b\u0010\u0017\u001a\u00020\u0014H\u0002J\u0012\u0010\u0018\u001a\u00020\u00142\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0014J\b\u0010\u001b\u001a\u00020\u0014H\u0014J\u0018\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0010\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\"H\u0002J\u0010\u0010#\u001a\u00020\u00142\u0006\u0010$\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006%"}, d2 = {"Lcom/safeword/ui/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/safeword/databinding/ActivityMainBinding;", "currentNativeAd", "Lcom/google/android/gms/ads/nativead/NativeAd;", "permissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "", "updatingModeSelection", "", "viewModel", "Lcom/safeword/ui/main/MainViewModel;", "getViewModel", "()Lcom/safeword/ui/main/MainViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "configureFeatureAccess", "", "ensureVoicePermission", "loadNativeAdIfNeeded", "observeState", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "populateNativeAdView", "nativeAd", "adBinding", "Lcom/safeword/databinding/ViewNativeAdBinding;", "renderState", "state", "Lcom/safeword/ui/main/MainUiState;", "updateModeButtons", "incomingSelected", "androidApp_freeDebug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.safeword.databinding.ActivityMainBinding binding;
    private boolean updatingModeSelection = false;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.ads.nativead.NativeAd currentNativeAd;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String[]> permissionLauncher = null;
    
    public MainActivity() {
        super();
    }
    
    private final com.safeword.ui.main.MainViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    private final void configureFeatureAccess() {
    }
    
    private final void loadNativeAdIfNeeded() {
    }
    
    private final void populateNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, com.safeword.databinding.ViewNativeAdBinding adBinding) {
    }
    
    private final void observeState() {
    }
    
    private final void renderState(com.safeword.ui.main.MainUiState state) {
    }
    
    private final void updateModeButtons(boolean incomingSelected) {
    }
    
    private final void ensureVoicePermission() {
    }
}