package com.safeword.service;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u0006\b\u0007\u0018\u0000 32\u00020\u00012\u00020\u0002:\u00013B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\u0014\u0010\u0016\u001a\u0004\u0018\u00010\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\u0012\u0010\u001a\u001a\u00020\u00152\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u0015H\u0016J\b\u0010\u001e\u001a\u00020\u0015H\u0016J\b\u0010\u001f\u001a\u00020\u0015H\u0016J\u0010\u0010 \u001a\u00020\u00152\u0006\u0010!\u001a\u00020\"H\u0016J\u001a\u0010#\u001a\u00020\u00152\u0006\u0010$\u001a\u00020\"2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\u0012\u0010\'\u001a\u00020\u00152\b\u0010(\u001a\u0004\u0018\u00010&H\u0016J\u0012\u0010)\u001a\u00020\u00152\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\u0012\u0010*\u001a\u00020\u00152\b\u0010+\u001a\u0004\u0018\u00010&H\u0016J\u0010\u0010,\u001a\u00020\u00152\u0006\u0010-\u001a\u00020.H\u0016J\"\u0010/\u001a\u00020\"2\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u00100\u001a\u00020\"2\u0006\u00101\u001a\u00020\"H\u0016J\b\u00102\u001a\u00020\u0015H\u0002R\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001e\u0010\n\u001a\u00020\u000b8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00064"}, d2 = {"Lcom/safeword/service/VoiceRecognitionService;", "Landroid/app/Service;", "Landroid/speech/RecognitionListener;", "()V", "engine", "Lcom/safeword/shared/domain/SafeWordEngine;", "getEngine", "()Lcom/safeword/shared/domain/SafeWordEngine;", "setEngine", "(Lcom/safeword/shared/domain/SafeWordEngine;)V", "notificationHelper", "Lcom/safeword/util/NotificationHelper;", "getNotificationHelper", "()Lcom/safeword/util/NotificationHelper;", "setNotificationHelper", "(Lcom/safeword/util/NotificationHelper;)V", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "speechRecognizer", "Landroid/speech/SpeechRecognizer;", "onBeginningOfSpeech", "", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onBufferReceived", "buffer", "", "onCreate", "onDestroy", "onEndOfSpeech", "onError", "error", "", "onEvent", "eventType", "params", "Landroid/os/Bundle;", "onPartialResults", "partialResults", "onReadyForSpeech", "onResults", "results", "onRmsChanged", "rmsdB", "", "onStartCommand", "flags", "startId", "startListening", "Companion", "androidApp_freeDebug"})
public final class VoiceRecognitionService extends android.app.Service implements android.speech.RecognitionListener {
    @javax.inject.Inject()
    public com.safeword.shared.domain.SafeWordEngine engine;
    @javax.inject.Inject()
    public com.safeword.util.NotificationHelper notificationHelper;
    @org.jetbrains.annotations.Nullable()
    private android.speech.SpeechRecognizer speechRecognizer;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.safeword.service.VoiceRecognitionService.Companion Companion = null;
    
    public VoiceRecognitionService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.SafeWordEngine getEngine() {
        return null;
    }
    
    public final void setEngine(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.SafeWordEngine p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.util.NotificationHelper getNotificationHelper() {
        return null;
    }
    
    public final void setNotificationHelper(@org.jetbrains.annotations.NotNull()
    com.safeword.util.NotificationHelper p0) {
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    private final void startListening() {
    }
    
    @java.lang.Override()
    public void onReadyForSpeech(@org.jetbrains.annotations.Nullable()
    android.os.Bundle params) {
    }
    
    @java.lang.Override()
    public void onBeginningOfSpeech() {
    }
    
    @java.lang.Override()
    public void onRmsChanged(float rmsdB) {
    }
    
    @java.lang.Override()
    public void onBufferReceived(@org.jetbrains.annotations.Nullable()
    byte[] buffer) {
    }
    
    @java.lang.Override()
    public void onEndOfSpeech() {
    }
    
    @java.lang.Override()
    public void onError(int error) {
    }
    
    @java.lang.Override()
    public void onResults(@org.jetbrains.annotations.Nullable()
    android.os.Bundle results) {
    }
    
    @java.lang.Override()
    public void onPartialResults(@org.jetbrains.annotations.Nullable()
    android.os.Bundle partialResults) {
    }
    
    @java.lang.Override()
    public void onEvent(int eventType, @org.jetbrains.annotations.Nullable()
    android.os.Bundle params) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\b"}, d2 = {"Lcom/safeword/service/VoiceRecognitionService$Companion;", "", "()V", "start", "", "context", "Landroid/content/Context;", "stop", "androidApp_freeDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void start(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
        
        public final void stop(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
    }
}