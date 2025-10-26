package com.safeword.data.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Lcom/safeword/data/db/SafeWordDatabase;", "Landroidx/room/RoomDatabase;", "()V", "alertEventDao", "Lcom/safeword/data/db/AlertEventDao;", "contactDao", "Lcom/safeword/data/db/ContactDao;", "androidApp_freeDebug"})
@androidx.room.Database(entities = {com.safeword.data.db.ContactEntity.class, com.safeword.data.db.AlertEventEntity.class}, version = 1, exportSchema = true)
public abstract class SafeWordDatabase extends androidx.room.RoomDatabase {
    
    public SafeWordDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.safeword.data.db.ContactDao contactDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.safeword.data.db.AlertEventDao alertEventDao();
}