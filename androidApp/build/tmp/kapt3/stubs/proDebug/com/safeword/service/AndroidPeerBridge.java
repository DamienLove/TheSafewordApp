package com.safeword.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 !2\u00020\u0001:\u0001!B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0096@\u00a2\u0006\u0002\u0010\u001cJ\u000e\u0010\u001d\u001a\u00020\u0019H\u0082@\u00a2\u0006\u0002\u0010\u001eJ\u000e\u0010\u001f\u001a\u00020\u0019H\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u000e\u0010 \u001a\u00020\u0019H\u0096@\u00a2\u0006\u0002\u0010\u001eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0018\u00010\u0011R\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\rR\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00140\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/safeword/service/AndroidPeerBridge;", "Lcom/safeword/shared/bridge/PeerBridge;", "context", "Landroid/content/Context;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "(Landroid/content/Context;Lkotlinx/coroutines/CoroutineScope;)V", "eventsFlow", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/safeword/shared/bridge/model/PeerBridgeEvent;", "incomingEvents", "Lkotlinx/coroutines/flow/Flow;", "getIncomingEvents", "()Lkotlinx/coroutines/flow/Flow;", "listenJob", "Lkotlinx/coroutines/Job;", "multicastLock", "Landroid/net/wifi/WifiManager$MulticastLock;", "Landroid/net/wifi/WifiManager;", "state", "Lcom/safeword/shared/bridge/model/PeerBridgeState;", "getState", "stateFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "broadcast", "", "event", "Lcom/safeword/shared/bridge/model/PeerBridgeEvent$AlertBroadcast;", "(Lcom/safeword/shared/bridge/model/PeerBridgeEvent$AlertBroadcast;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "runListener", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startAdvertising", "stop", "Companion", "androidApp_proDebug"})
public final class AndroidPeerBridge implements com.safeword.shared.bridge.PeerBridge {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.safeword.shared.bridge.model.PeerBridgeState> stateFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.safeword.shared.bridge.model.PeerBridgeEvent> eventsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.safeword.shared.bridge.model.PeerBridgeState> state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.safeword.shared.bridge.model.PeerBridgeEvent> incomingEvents = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job listenJob;
    @org.jetbrains.annotations.Nullable()
    private android.net.wifi.WifiManager.MulticastLock multicastLock;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String MULTICAST_ADDRESS = "224.1.1.29";
    private static final int MULTICAST_PORT = 45230;
    @org.jetbrains.annotations.NotNull()
    public static final com.safeword.service.AndroidPeerBridge.Companion Companion = null;
    
    public AndroidPeerBridge(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineScope scope) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.safeword.shared.bridge.model.PeerBridgeState> getState() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.safeword.shared.bridge.model.PeerBridgeEvent> getIncomingEvents() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object startAdvertising(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object stop(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object broadcast(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.bridge.model.PeerBridgeEvent.AlertBroadcast event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object runListener(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/safeword/service/AndroidPeerBridge$Companion;", "", "()V", "MULTICAST_ADDRESS", "", "MULTICAST_PORT", "", "androidApp_proDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}