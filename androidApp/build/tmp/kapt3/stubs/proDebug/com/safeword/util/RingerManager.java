package com.safeword.util;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ\b\u0010\u000e\u001a\u00020\rH\u0002J\u001a\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00062\b\u0010\u0011\u001a\u0004\u0018\u00010\bH\u0002R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/safeword/util/RingerManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "audioManager", "Landroid/media/AudioManager;", "notificationManager", "Landroid/app/NotificationManager;", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "raiseToMax", "", "scheduleRestore", "storeStateIfNeeded", "audio", "notification", "Companion", "androidApp_proDebug"})
public final class RingerManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.Nullable()
    private final android.media.AudioManager audioManager = null;
    @org.jetbrains.annotations.Nullable()
    private final android.app.NotificationManager notificationManager = null;
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PREFS_NAME = "safeword_ringer_state";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_RING_VOLUME = "ring_volume";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_NOTIFICATION_VOLUME = "notification_volume";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_ALARM_VOLUME = "alarm_volume";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_RINGER_MODE = "ringer_mode";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String KEY_INTERRUPT_FILTER = "interrupt_filter";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String RESTORE_WORK_NAME = "restore_ringer_work";
    @org.jetbrains.annotations.NotNull()
    public static final com.safeword.util.RingerManager.Companion Companion = null;
    
    public RingerManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void raiseToMax() {
    }
    
    private final void storeStateIfNeeded(android.media.AudioManager audio, android.app.NotificationManager notification) {
    }
    
    private final void scheduleRestore() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/safeword/util/RingerManager$Companion;", "", "()V", "KEY_ALARM_VOLUME", "", "KEY_INTERRUPT_FILTER", "KEY_NOTIFICATION_VOLUME", "KEY_RINGER_MODE", "KEY_RING_VOLUME", "PREFS_NAME", "RESTORE_WORK_NAME", "androidApp_proDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}