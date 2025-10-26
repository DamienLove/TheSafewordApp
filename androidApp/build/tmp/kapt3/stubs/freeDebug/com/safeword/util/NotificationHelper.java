package com.safeword.util;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\nJ\u0006\u0010\u0010\u001a\u00020\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/safeword/util/NotificationHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "manager", "Landroid/app/NotificationManager;", "buildAlertNotification", "Landroid/app/Notification;", "detectedWord", "", "buildListeningNotification", "isActive", "", "buildPeerNotification", "stateDescription", "ensureChannels", "", "Companion", "androidApp_freeDebug"})
public final class NotificationHelper {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.app.NotificationManager manager = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_LISTENING = "safeword_listening";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ALERTS = "safeword_alerts";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_PEER = "safeword_peer";
    public static final int NOTIFICATION_ID_LISTENING = 1;
    public static final int NOTIFICATION_ID_ALERT = 2;
    public static final int NOTIFICATION_ID_PEER = 3;
    @org.jetbrains.annotations.NotNull()
    public static final com.safeword.util.NotificationHelper.Companion Companion = null;
    
    public NotificationHelper(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void ensureChannels() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.app.Notification buildListeningNotification(boolean isActive) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.app.Notification buildAlertNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String detectedWord) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.app.Notification buildPeerNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String stateDescription) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/safeword/util/NotificationHelper$Companion;", "", "()V", "CHANNEL_ALERTS", "", "CHANNEL_LISTENING", "CHANNEL_PEER", "NOTIFICATION_ID_ALERT", "", "NOTIFICATION_ID_LISTENING", "NOTIFICATION_ID_PEER", "androidApp_freeDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}