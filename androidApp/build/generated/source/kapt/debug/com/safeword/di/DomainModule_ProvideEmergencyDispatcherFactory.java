package com.safeword.di;

import android.content.Context;
import com.safeword.shared.domain.service.EmergencyDispatcher;
import com.safeword.util.LocationHelper;
import com.safeword.util.NotificationHelper;
import com.safeword.util.RingerManager;
import com.safeword.util.SmsSender;
import com.safeword.util.SoundPlayer;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class DomainModule_ProvideEmergencyDispatcherFactory implements Factory<EmergencyDispatcher> {
  private final Provider<Context> contextProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private final Provider<RingerManager> ringerManagerProvider;

  private final Provider<SoundPlayer> soundPlayerProvider;

  private final Provider<SmsSender> smsSenderProvider;

  private final Provider<LocationHelper> locationHelperProvider;

  public DomainModule_ProvideEmergencyDispatcherFactory(Provider<Context> contextProvider,
      Provider<NotificationHelper> notificationHelperProvider,
      Provider<RingerManager> ringerManagerProvider, Provider<SoundPlayer> soundPlayerProvider,
      Provider<SmsSender> smsSenderProvider, Provider<LocationHelper> locationHelperProvider) {
    this.contextProvider = contextProvider;
    this.notificationHelperProvider = notificationHelperProvider;
    this.ringerManagerProvider = ringerManagerProvider;
    this.soundPlayerProvider = soundPlayerProvider;
    this.smsSenderProvider = smsSenderProvider;
    this.locationHelperProvider = locationHelperProvider;
  }

  @Override
  public EmergencyDispatcher get() {
    return provideEmergencyDispatcher(contextProvider.get(), notificationHelperProvider.get(), ringerManagerProvider.get(), soundPlayerProvider.get(), smsSenderProvider.get(), locationHelperProvider.get());
  }

  public static DomainModule_ProvideEmergencyDispatcherFactory create(
      Provider<Context> contextProvider, Provider<NotificationHelper> notificationHelperProvider,
      Provider<RingerManager> ringerManagerProvider, Provider<SoundPlayer> soundPlayerProvider,
      Provider<SmsSender> smsSenderProvider, Provider<LocationHelper> locationHelperProvider) {
    return new DomainModule_ProvideEmergencyDispatcherFactory(contextProvider, notificationHelperProvider, ringerManagerProvider, soundPlayerProvider, smsSenderProvider, locationHelperProvider);
  }

  public static EmergencyDispatcher provideEmergencyDispatcher(Context context,
      NotificationHelper notificationHelper, RingerManager ringerManager, SoundPlayer soundPlayer,
      SmsSender smsSender, LocationHelper locationHelper) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideEmergencyDispatcher(context, notificationHelper, ringerManager, soundPlayer, smsSender, locationHelper));
  }
}
