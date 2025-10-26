package com.safeword.receiver;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aH\u0082@\u00a2\u0006\u0002\u0010\u001cJ\u001c\u0010\u001d\u001a\u00020\u00182\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0011\u001a\u00020\u00128\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016\u00a8\u0006#"}, d2 = {"Lcom/safeword/receiver/SafeWordSmsReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "contactRepository", "Lcom/safeword/shared/domain/repository/ContactRepository;", "getContactRepository", "()Lcom/safeword/shared/domain/repository/ContactRepository;", "setContactRepository", "(Lcom/safeword/shared/domain/repository/ContactRepository;)V", "engine", "Lcom/safeword/shared/domain/SafeWordEngine;", "getEngine", "()Lcom/safeword/shared/domain/SafeWordEngine;", "setEngine", "(Lcom/safeword/shared/domain/SafeWordEngine;)V", "scope", "Lkotlinx/coroutines/CoroutineScope;", "upsertContactUseCase", "Lcom/safeword/shared/domain/usecase/UpsertContactUseCase;", "getUpsertContactUseCase", "()Lcom/safeword/shared/domain/usecase/UpsertContactUseCase;", "setUpsertContactUseCase", "(Lcom/safeword/shared/domain/usecase/UpsertContactUseCase;)V", "ensureContactForSender", "", "phone", "", "message", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onReceive", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "Companion", "androidApp_freeDebug"})
public final class SafeWordSmsReceiver extends android.content.BroadcastReceiver {
    @javax.inject.Inject()
    public com.safeword.shared.domain.SafeWordEngine engine;
    @javax.inject.Inject()
    public com.safeword.shared.domain.usecase.UpsertContactUseCase upsertContactUseCase;
    @javax.inject.Inject()
    public com.safeword.shared.domain.repository.ContactRepository contactRepository;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "SafeWordSmsReceiver";
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex safeWordNameRegex = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.safeword.receiver.SafeWordSmsReceiver.Companion Companion = null;
    
    public SafeWordSmsReceiver() {
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
    public final com.safeword.shared.domain.usecase.UpsertContactUseCase getUpsertContactUseCase() {
        return null;
    }
    
    public final void setUpsertContactUseCase(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.usecase.UpsertContactUseCase p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.repository.ContactRepository getContactRepository() {
        return null;
    }
    
    public final void setContactRepository(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.ContactRepository p0) {
    }
    
    @java.lang.Override()
    public void onReceive(@org.jetbrains.annotations.Nullable()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
    }
    
    private final java.lang.Object ensureContactForSender(java.lang.String phone, java.lang.String message, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/safeword/receiver/SafeWordSmsReceiver$Companion;", "", "()V", "TAG", "", "safeWordNameRegex", "Lkotlin/text/Regex;", "androidApp_freeDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}