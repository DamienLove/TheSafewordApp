package com.safeword.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0016H\u0096@\u00a2\u0006\u0002\u0010\u0017J\u000e\u0010\u0018\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001a\u001a\u0010\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001c\u0018\u00010\u001bH\u0096@\u00a2\u0006\u0002\u0010\u0019J$\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020#0\"H\u0096@\u00a2\u0006\u0002\u0010$J\u0016\u0010%\u001a\u00020\u00102\u0006\u0010&\u001a\u00020 H\u0096@\u00a2\u0006\u0002\u0010\'R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/safeword/service/AndroidEmergencyDispatcher;", "Lcom/safeword/shared/domain/service/EmergencyDispatcher;", "context", "Landroid/content/Context;", "notificationHelper", "Lcom/safeword/util/NotificationHelper;", "ringerManager", "Lcom/safeword/util/RingerManager;", "soundPlayer", "Lcom/safeword/util/SoundPlayer;", "smsSender", "Lcom/safeword/util/SmsSender;", "locationHelper", "Lcom/safeword/util/LocationHelper;", "(Landroid/content/Context;Lcom/safeword/util/NotificationHelper;Lcom/safeword/util/RingerManager;Lcom/safeword/util/SoundPlayer;Lcom/safeword/util/SmsSender;Lcom/safeword/util/LocationHelper;)V", "logEvent", "", "event", "Lcom/safeword/shared/domain/model/AlertEvent;", "(Lcom/safeword/shared/domain/model/AlertEvent;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "playSiren", "enabled", "", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "raiseRinger", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resolveLocation", "Lkotlin/Pair;", "", "sendSms", "", "message", "", "contacts", "", "Lcom/safeword/shared/domain/model/Contact;", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "showEmergencyPrompt", "detectedWord", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "androidApp_proDebug"})
public final class AndroidEmergencyDispatcher implements com.safeword.shared.domain.service.EmergencyDispatcher {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.util.NotificationHelper notificationHelper = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.util.RingerManager ringerManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.util.SoundPlayer soundPlayer = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.util.SmsSender smsSender = null;
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.util.LocationHelper locationHelper = null;
    
    public AndroidEmergencyDispatcher(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.safeword.util.NotificationHelper notificationHelper, @org.jetbrains.annotations.NotNull()
    com.safeword.util.RingerManager ringerManager, @org.jetbrains.annotations.NotNull()
    com.safeword.util.SoundPlayer soundPlayer, @org.jetbrains.annotations.NotNull()
    com.safeword.util.SmsSender smsSender, @org.jetbrains.annotations.NotNull()
    com.safeword.util.LocationHelper locationHelper) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object raiseRinger(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object playSiren(boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object resolveLocation(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Double, java.lang.Double>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object sendSms(@org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    java.util.List<com.safeword.shared.domain.model.Contact> contacts, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object showEmergencyPrompt(@org.jetbrains.annotations.NotNull()
    java.lang.String detectedWord, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object logEvent(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.model.AlertEvent event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}