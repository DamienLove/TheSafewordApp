package com.safeword.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u009e\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J:\u0010\t\u001a\u00020\n2\b\b\u0001\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0007J\u0012\u0010\u0017\u001a\u00020\u00162\b\b\u0001\u0010\u000b\u001a\u00020\fH\u0007J\u0012\u0010\u0018\u001a\u00020\u000e2\b\b\u0001\u0010\u000b\u001a\u00020\fH\u0007J\u001a\u0010\u0019\u001a\u00020\u001a2\b\b\u0001\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\u0004H\u0007J\u0012\u0010\u001c\u001a\u00020\u00102\b\b\u0001\u0010\u000b\u001a\u00020\fH\u0007J@\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\n2\u0006\u0010$\u001a\u00020\u001a2\u0006\u0010%\u001a\u00020&2\u0006\u0010\u001b\u001a\u00020\u0004H\u0007J\b\u0010\'\u001a\u00020\u0014H\u0007J\u0012\u0010(\u001a\u00020\u00122\b\b\u0001\u0010\u000b\u001a\u00020\fH\u0007J\b\u0010)\u001a\u00020&H\u0007J\u0010\u0010*\u001a\u00020+2\u0006\u0010\u001f\u001a\u00020 H\u0007J\u0010\u0010,\u001a\u00020-2\u0006\u0010\u001f\u001a\u00020 H\u0007J\u0010\u0010.\u001a\u00020/2\u0006\u0010\u001f\u001a\u00020 H\u0007J\u0010\u00100\u001a\u0002012\u0006\u0010\u001f\u001a\u00020 H\u0007J\u0010\u00102\u001a\u0002032\u0006\u0010\u001f\u001a\u00020 H\u0007J\u0010\u00104\u001a\u0002052\u0006\u0010\u001f\u001a\u00020 H\u0007J\u0010\u00106\u001a\u0002072\u0006\u0010\u001f\u001a\u00020 H\u0007J\u0018\u00108\u001a\u0002092\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010%\u001a\u00020&H\u0007\u00a8\u0006:"}, d2 = {"Lcom/safeword/di/DomainModule;", "", "()V", "provideAppScope", "Lkotlinx/coroutines/CoroutineScope;", "provideDeleteContactUseCase", "Lcom/safeword/shared/domain/usecase/DeleteContactUseCase;", "contactRepository", "Lcom/safeword/shared/domain/repository/ContactRepository;", "provideEmergencyDispatcher", "Lcom/safeword/shared/domain/service/EmergencyDispatcher;", "context", "Landroid/content/Context;", "notificationHelper", "Lcom/safeword/util/NotificationHelper;", "ringerManager", "Lcom/safeword/util/RingerManager;", "soundPlayer", "Lcom/safeword/util/SoundPlayer;", "smsSender", "Lcom/safeword/util/SmsSender;", "locationHelper", "Lcom/safeword/util/LocationHelper;", "provideLocationHelper", "provideNotificationHelper", "providePeerBridge", "Lcom/safeword/shared/bridge/PeerBridge;", "scope", "provideRingerManager", "provideSafeWordEngine", "Lcom/safeword/shared/domain/SafeWordEngine;", "settingsGateway", "Lcom/safeword/shared/domain/repository/SettingsGateway;", "alertEventRepository", "Lcom/safeword/shared/domain/repository/AlertEventRepository;", "dispatcher", "peerBridge", "timeProvider", "Lcom/safeword/shared/util/TimeProvider;", "provideSmsSender", "provideSoundPlayer", "provideTimeProvider", "provideToggleIncludeLocationUseCase", "Lcom/safeword/shared/domain/usecase/ToggleIncludeLocationUseCase;", "provideToggleIncomingModeUseCase", "Lcom/safeword/shared/domain/usecase/ToggleIncomingModeUseCase;", "provideToggleListeningUseCase", "Lcom/safeword/shared/domain/usecase/ToggleListeningUseCase;", "provideTogglePlaySirenUseCase", "Lcom/safeword/shared/domain/usecase/TogglePlaySirenUseCase;", "provideToggleTestModeUseCase", "Lcom/safeword/shared/domain/usecase/ToggleTestModeUseCase;", "provideUpdateSafeWordsUseCase", "Lcom/safeword/shared/domain/usecase/UpdateSafeWordsUseCase;", "provideUpdateSensitivityUseCase", "Lcom/safeword/shared/domain/usecase/UpdateSensitivityUseCase;", "provideUpsertContactUseCase", "Lcom/safeword/shared/domain/usecase/UpsertContactUseCase;", "androidApp_proDebug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class DomainModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.safeword.di.DomainModule INSTANCE = null;
    
    private DomainModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.util.NotificationHelper provideNotificationHelper(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.util.RingerManager provideRingerManager(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.util.SoundPlayer provideSoundPlayer(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.util.LocationHelper provideLocationHelper(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.util.SmsSender provideSmsSender() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.service.EmergencyDispatcher provideEmergencyDispatcher(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.safeword.util.NotificationHelper notificationHelper, @org.jetbrains.annotations.NotNull()
    com.safeword.util.RingerManager ringerManager, @org.jetbrains.annotations.NotNull()
    com.safeword.util.SoundPlayer soundPlayer, @org.jetbrains.annotations.NotNull()
    com.safeword.util.SmsSender smsSender, @org.jetbrains.annotations.NotNull()
    com.safeword.util.LocationHelper locationHelper) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.CoroutineScope provideAppScope() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.bridge.PeerBridge providePeerBridge(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineScope scope) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.util.TimeProvider provideTimeProvider() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.SafeWordEngine provideSafeWordEngine(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.SettingsGateway settingsGateway, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.ContactRepository contactRepository, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.AlertEventRepository alertEventRepository, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.service.EmergencyDispatcher dispatcher, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.bridge.PeerBridge peerBridge, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.util.TimeProvider timeProvider, @org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineScope scope) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.usecase.UpdateSafeWordsUseCase provideUpdateSafeWordsUseCase(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.SettingsGateway settingsGateway) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.usecase.UpdateSensitivityUseCase provideUpdateSensitivityUseCase(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.SettingsGateway settingsGateway) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.usecase.ToggleListeningUseCase provideToggleListeningUseCase(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.SettingsGateway settingsGateway) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.usecase.ToggleIncomingModeUseCase provideToggleIncomingModeUseCase(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.SettingsGateway settingsGateway) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.usecase.ToggleIncludeLocationUseCase provideToggleIncludeLocationUseCase(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.SettingsGateway settingsGateway) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.usecase.TogglePlaySirenUseCase provideTogglePlaySirenUseCase(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.SettingsGateway settingsGateway) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.usecase.ToggleTestModeUseCase provideToggleTestModeUseCase(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.SettingsGateway settingsGateway) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.usecase.UpsertContactUseCase provideUpsertContactUseCase(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.ContactRepository contactRepository, @org.jetbrains.annotations.NotNull()
    com.safeword.shared.util.TimeProvider timeProvider) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.safeword.shared.domain.usecase.DeleteContactUseCase provideDeleteContactUseCase(@org.jetbrains.annotations.NotNull()
    com.safeword.shared.domain.repository.ContactRepository contactRepository) {
        return null;
    }
}