package com.safeword.util;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/safeword/util/SmsSender;", "", "smsManager", "Landroid/telephony/SmsManager;", "(Landroid/telephony/SmsManager;)V", "send", "", "message", "", "contacts", "", "Lcom/safeword/shared/domain/model/Contact;", "androidApp_proDebug"})
public final class SmsSender {
    @org.jetbrains.annotations.NotNull()
    private final android.telephony.SmsManager smsManager = null;
    
    public SmsSender(@org.jetbrains.annotations.NotNull()
    android.telephony.SmsManager smsManager) {
        super();
    }
    
    public final int send(@org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    java.util.List<com.safeword.shared.domain.model.Contact> contacts) {
        return 0;
    }
    
    public SmsSender() {
        super();
    }
}