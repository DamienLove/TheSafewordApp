package com.safeword.data.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\b\n\u0000\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\b2\u0006\u0010\n\u001a\u00020\u000bH\'\u00a8\u0006\f"}, d2 = {"Lcom/safeword/data/db/AlertEventDao;", "", "insert", "", "event", "Lcom/safeword/data/db/AlertEventEntity;", "(Lcom/safeword/data/db/AlertEventEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeLatest", "Lkotlinx/coroutines/flow/Flow;", "", "limit", "", "androidApp_proDebug"})
@androidx.room.Dao()
public abstract interface AlertEventDao {
    
    @androidx.room.Query(value = "SELECT * FROM alert_events ORDER BY timestamp DESC LIMIT :limit")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.safeword.data.db.AlertEventEntity>> observeLatest(int limit);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.safeword.data.db.AlertEventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
}