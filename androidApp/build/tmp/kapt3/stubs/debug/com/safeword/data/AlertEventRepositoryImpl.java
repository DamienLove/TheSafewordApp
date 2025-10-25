package com.safeword.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\t\u001a\u00020\nH\u0016J\u0016\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\rJ\f\u0010\u000e\u001a\u00020\b*\u00020\u000fH\u0002J\f\u0010\u0010\u001a\u00020\u000f*\u00020\bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/safeword/data/AlertEventRepositoryImpl;", "Lcom/safeword/shared/domain/repository/AlertEventRepository;", "dao", "Lcom/safeword/data/db/AlertEventDao;", "(Lcom/safeword/data/db/AlertEventDao;)V", "observeLatest", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/safeword/shared/domain/model/AlertEvent;", "limit", "", "record", "event", "(Lcom/safeword/shared/domain/model/AlertEvent;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toDomain", "Lcom/safeword/data/db/AlertEventEntity;", "toEntity", "androidApp_debug"})
public final class AlertEventRepositoryImpl implements com.safeword.shared.domain.repository.AlertEventRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.data.db.AlertEventDao dao = null;
    
    public AlertEventRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.safeword.data.db.AlertEventDao dao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.safeword.shared.domain.model.AlertEvent>> observeLatest(int limit) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object record(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.model.AlertEvent event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.safeword.shared.domain.model.AlertEvent> $completion) {
        return null;
    }
    
    private final com.safeword.shared.domain.model.AlertEvent toDomain(com.safeword.data.db.AlertEventEntity $this$toDomain) {
        return null;
    }
    
    private final com.safeword.data.db.AlertEventEntity toEntity(com.safeword.shared.domain.model.AlertEvent $this$toEntity) {
        return null;
    }
}