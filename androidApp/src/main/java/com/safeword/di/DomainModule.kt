package com.safeword.di

import android.content.Context
import com.safeword.service.AndroidEmergencyDispatcher
import com.safeword.service.AndroidPeerBridge
import com.safeword.shared.SafeWordBootstrap
import com.safeword.shared.bridge.PeerBridge
import com.safeword.shared.domain.SafeWordEngine
import com.safeword.shared.domain.repository.AlertEventRepository
import com.safeword.shared.domain.repository.ContactRepository
import com.safeword.shared.domain.repository.SettingsGateway
import com.safeword.shared.domain.service.EmergencyDispatcher
import com.safeword.shared.domain.usecase.DeleteContactUseCase
import com.safeword.shared.domain.usecase.ToggleIncludeLocationUseCase
import com.safeword.shared.domain.usecase.ToggleIncomingModeUseCase
import com.safeword.shared.domain.usecase.ToggleListeningUseCase
import com.safeword.shared.domain.usecase.TogglePlaySirenUseCase
import com.safeword.shared.domain.usecase.ToggleTestModeUseCase
import com.safeword.shared.domain.usecase.UpdateSafeWordsUseCase
import com.safeword.shared.domain.usecase.UpdateSensitivityUseCase
import com.safeword.shared.domain.usecase.UpsertContactUseCase
import com.safeword.shared.util.DefaultTimeProvider
import com.safeword.shared.util.TimeProvider
import com.safeword.util.LocationHelper
import com.safeword.util.NotificationHelper
import com.safeword.util.RingerManager
import com.safeword.util.SmsSender
import com.safeword.util.SoundPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideNotificationHelper(@ApplicationContext context: Context): NotificationHelper =
        NotificationHelper(context).apply { ensureChannels() }

    @Provides
    @Singleton
    fun provideRingerManager(@ApplicationContext context: Context) = RingerManager(context)

    @Provides
    @Singleton
    fun provideSoundPlayer(@ApplicationContext context: Context) = SoundPlayer(context)

    @Provides
    @Singleton
    fun provideLocationHelper(@ApplicationContext context: Context) = LocationHelper(context)

    @Provides
    @Singleton
    fun provideSmsSender(): SmsSender = SmsSender()

    @Provides
    @Singleton
    fun provideEmergencyDispatcher(
        @ApplicationContext context: Context,
        notificationHelper: NotificationHelper,
        ringerManager: RingerManager,
        soundPlayer: SoundPlayer,
        smsSender: SmsSender,
        locationHelper: LocationHelper
    ): EmergencyDispatcher = AndroidEmergencyDispatcher(
        context = context,
        notificationHelper = notificationHelper,
        ringerManager = ringerManager,
        soundPlayer = soundPlayer,
        smsSender = smsSender,
        locationHelper = locationHelper
    )

    @Provides
    @Singleton
    fun provideAppScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Provides
    @Singleton
    fun providePeerBridge(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): PeerBridge = AndroidPeerBridge(context, scope)

    @Provides
    @Singleton
    fun provideTimeProvider(): TimeProvider = DefaultTimeProvider()

    @Provides
    @Singleton
    fun provideSafeWordEngine(
        settingsGateway: SettingsGateway,
        contactRepository: ContactRepository,
        alertEventRepository: AlertEventRepository,
        dispatcher: EmergencyDispatcher,
        peerBridge: PeerBridge,
        timeProvider: TimeProvider,
        scope: CoroutineScope
    ): SafeWordEngine = SafeWordBootstrap.createEngine(
        settingsGateway = settingsGateway,
        contactRepository = contactRepository,
        alertEventRepository = alertEventRepository,
        emergencyDispatcher = dispatcher,
        peerBridge = peerBridge,
        timeProvider = timeProvider,
        scope = scope
    )

    @Provides
    fun provideUpdateSafeWordsUseCase(settingsGateway: SettingsGateway) =
        UpdateSafeWordsUseCase(settingsGateway)

    @Provides
    fun provideUpdateSensitivityUseCase(settingsGateway: SettingsGateway) =
        UpdateSensitivityUseCase(settingsGateway)

    @Provides
    fun provideToggleListeningUseCase(settingsGateway: SettingsGateway) =
        ToggleListeningUseCase(settingsGateway)

    @Provides
    fun provideToggleIncomingModeUseCase(settingsGateway: SettingsGateway) =
        ToggleIncomingModeUseCase(settingsGateway)

    @Provides
    fun provideToggleIncludeLocationUseCase(settingsGateway: SettingsGateway) =
        ToggleIncludeLocationUseCase(settingsGateway)

    @Provides
    fun provideTogglePlaySirenUseCase(settingsGateway: SettingsGateway) =
        TogglePlaySirenUseCase(settingsGateway)

    @Provides
    fun provideToggleTestModeUseCase(settingsGateway: SettingsGateway) =
        ToggleTestModeUseCase(settingsGateway)

    @Provides
    fun provideUpsertContactUseCase(
        contactRepository: ContactRepository,
        timeProvider: TimeProvider
    ) = UpsertContactUseCase(contactRepository, timeProvider)

    @Provides
    fun provideDeleteContactUseCase(contactRepository: ContactRepository) =
        DeleteContactUseCase(contactRepository)
}
