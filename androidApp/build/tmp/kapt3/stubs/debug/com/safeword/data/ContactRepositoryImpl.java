package com.safeword.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u000e0\rH\u0016J\u0016\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u0011J\f\u0010\u0012\u001a\u00020\u000b*\u00020\u0013H\u0002J\f\u0010\u0014\u001a\u00020\u0013*\u00020\u000bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/safeword/data/ContactRepositoryImpl;", "Lcom/safeword/shared/domain/repository/ContactRepository;", "dao", "Lcom/safeword/data/db/ContactDao;", "(Lcom/safeword/data/db/ContactDao;)V", "delete", "", "contactId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getContact", "Lcom/safeword/shared/domain/model/Contact;", "observeContacts", "Lkotlinx/coroutines/flow/Flow;", "", "upsert", "contact", "(Lcom/safeword/shared/domain/model/Contact;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toDomain", "Lcom/safeword/data/db/ContactEntity;", "toEntity", "androidApp_debug"})
public final class ContactRepositoryImpl implements com.safeword.shared.domain.repository.ContactRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.safeword.data.db.ContactDao dao = null;
    
    public ContactRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.safeword.data.db.ContactDao dao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.safeword.shared.domain.model.Contact>> observeContacts() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.model.Contact contact, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.safeword.shared.domain.model.Contact> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object delete(long contactId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getContact(long contactId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.safeword.shared.domain.model.Contact> $completion) {
        return null;
    }
    
    private final com.safeword.shared.domain.model.Contact toDomain(com.safeword.data.db.ContactEntity $this$toDomain) {
        return null;
    }
    
    private final com.safeword.data.db.ContactEntity toEntity(com.safeword.shared.domain.model.Contact $this$toEntity) {
        return null;
    }
}