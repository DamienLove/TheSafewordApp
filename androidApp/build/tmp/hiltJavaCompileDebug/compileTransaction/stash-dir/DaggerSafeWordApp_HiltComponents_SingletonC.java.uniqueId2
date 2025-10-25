package com.safeword;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.safeword.data.db.AlertEventDao;
import com.safeword.data.db.ContactDao;
import com.safeword.data.db.SafeWordDatabase;
import com.safeword.di.DatabaseModule_ProvideAlertEventDaoFactory;
import com.safeword.di.DatabaseModule_ProvideContactDaoFactory;
import com.safeword.di.DatabaseModule_ProvideDatabaseFactory;
import com.safeword.di.DomainModule_ProvideAppScopeFactory;
import com.safeword.di.DomainModule_ProvideDeleteContactUseCaseFactory;
import com.safeword.di.DomainModule_ProvideEmergencyDispatcherFactory;
import com.safeword.di.DomainModule_ProvideLocationHelperFactory;
import com.safeword.di.DomainModule_ProvideNotificationHelperFactory;
import com.safeword.di.DomainModule_ProvidePeerBridgeFactory;
import com.safeword.di.DomainModule_ProvideRingerManagerFactory;
import com.safeword.di.DomainModule_ProvideSafeWordEngineFactory;
import com.safeword.di.DomainModule_ProvideSmsSenderFactory;
import com.safeword.di.DomainModule_ProvideSoundPlayerFactory;
import com.safeword.di.DomainModule_ProvideTimeProviderFactory;
import com.safeword.di.DomainModule_ProvideToggleIncludeLocationUseCaseFactory;
import com.safeword.di.DomainModule_ProvideToggleIncomingModeUseCaseFactory;
import com.safeword.di.DomainModule_ProvideToggleListeningUseCaseFactory;
import com.safeword.di.DomainModule_ProvideTogglePlaySirenUseCaseFactory;
import com.safeword.di.DomainModule_ProvideToggleTestModeUseCaseFactory;
import com.safeword.di.DomainModule_ProvideUpdateSafeWordsUseCaseFactory;
import com.safeword.di.DomainModule_ProvideUpdateSensitivityUseCaseFactory;
import com.safeword.di.DomainModule_ProvideUpsertContactUseCaseFactory;
import com.safeword.di.GatewayModule_ProvideAlertEventRepositoryFactory;
import com.safeword.di.GatewayModule_ProvideContactRepositoryFactory;
import com.safeword.di.GatewayModule_ProvideDataStoreFactory;
import com.safeword.di.GatewayModule_ProvideSettingsGatewayFactory;
import com.safeword.receiver.SafeWordSmsReceiver;
import com.safeword.receiver.SafeWordSmsReceiver_MembersInjector;
import com.safeword.service.EmergencyHandlerService;
import com.safeword.service.EmergencyHandlerService_MembersInjector;
import com.safeword.service.SafeWordPeerService;
import com.safeword.service.SafeWordPeerService_MembersInjector;
import com.safeword.service.VoiceRecognitionService;
import com.safeword.service.VoiceRecognitionService_MembersInjector;
import com.safeword.shared.bridge.PeerBridge;
import com.safeword.shared.domain.SafeWordEngine;
import com.safeword.shared.domain.repository.AlertEventRepository;
import com.safeword.shared.domain.repository.ContactRepository;
import com.safeword.shared.domain.repository.SettingsGateway;
import com.safeword.shared.domain.service.EmergencyDispatcher;
import com.safeword.shared.domain.usecase.DeleteContactUseCase;
import com.safeword.shared.domain.usecase.ToggleIncludeLocationUseCase;
import com.safeword.shared.domain.usecase.ToggleIncomingModeUseCase;
import com.safeword.shared.domain.usecase.ToggleListeningUseCase;
import com.safeword.shared.domain.usecase.TogglePlaySirenUseCase;
import com.safeword.shared.domain.usecase.ToggleTestModeUseCase;
import com.safeword.shared.domain.usecase.UpdateSafeWordsUseCase;
import com.safeword.shared.domain.usecase.UpdateSensitivityUseCase;
import com.safeword.shared.domain.usecase.UpsertContactUseCase;
import com.safeword.shared.util.TimeProvider;
import com.safeword.ui.ContactsActivity;
import com.safeword.ui.MainActivity;
import com.safeword.ui.SettingsActivity;
import com.safeword.ui.contacts.ContactsViewModel;
import com.safeword.ui.contacts.ContactsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.safeword.ui.main.MainViewModel;
import com.safeword.ui.main.MainViewModel_HiltModules_KeyModule_ProvideFactory;
import com.safeword.ui.settings.SettingsViewModel;
import com.safeword.ui.settings.SettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.safeword.util.LocationHelper;
import com.safeword.util.NotificationHelper;
import com.safeword.util.RingerManager;
import com.safeword.util.SmsSender;
import com.safeword.util.SoundPlayer;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SetBuilder;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineScope;

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
public final class DaggerSafeWordApp_HiltComponents_SingletonC {
  private DaggerSafeWordApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public SafeWordApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements SafeWordApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public SafeWordApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements SafeWordApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public SafeWordApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements SafeWordApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public SafeWordApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements SafeWordApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SafeWordApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements SafeWordApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SafeWordApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements SafeWordApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public SafeWordApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements SafeWordApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public SafeWordApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends SafeWordApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends SafeWordApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends SafeWordApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends SafeWordApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectContactsActivity(ContactsActivity contactsActivity) {
    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public void injectSettingsActivity(SettingsActivity settingsActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return SetBuilder.<String>newSetBuilder(3).add(ContactsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(MainViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(SettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).build();
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends SafeWordApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<ContactsViewModel> contactsViewModelProvider;

    private Provider<MainViewModel> mainViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.contactsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.mainViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
    }

    @Override
    public Map<String, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(3).put("com.safeword.ui.contacts.ContactsViewModel", ((Provider) contactsViewModelProvider)).put("com.safeword.ui.main.MainViewModel", ((Provider) mainViewModelProvider)).put("com.safeword.ui.settings.SettingsViewModel", ((Provider) settingsViewModelProvider)).build();
    }

    @Override
    public Map<String, Object> getHiltViewModelAssistedMap() {
      return Collections.<String, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.safeword.ui.contacts.ContactsViewModel 
          return (T) new ContactsViewModel(singletonCImpl.provideContactRepositoryProvider.get(), singletonCImpl.upsertContactUseCase(), singletonCImpl.deleteContactUseCase());

          case 1: // com.safeword.ui.main.MainViewModel 
          return (T) new MainViewModel(singletonCImpl.provideSafeWordEngineProvider.get(), singletonCImpl.toggleListeningUseCase(), singletonCImpl.toggleIncomingModeUseCase(), singletonCImpl.toggleTestModeUseCase());

          case 2: // com.safeword.ui.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.provideSettingsGatewayProvider.get(), singletonCImpl.updateSafeWordsUseCase(), singletonCImpl.updateSensitivityUseCase(), singletonCImpl.toggleIncludeLocationUseCase(), singletonCImpl.togglePlaySirenUseCase(), singletonCImpl.toggleTestModeUseCase(), singletonCImpl.provideSafeWordEngineProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends SafeWordApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends SafeWordApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectEmergencyHandlerService(EmergencyHandlerService emergencyHandlerService) {
      injectEmergencyHandlerService2(emergencyHandlerService);
    }

    @Override
    public void injectSafeWordPeerService(SafeWordPeerService safeWordPeerService) {
      injectSafeWordPeerService2(safeWordPeerService);
    }

    @Override
    public void injectVoiceRecognitionService(VoiceRecognitionService voiceRecognitionService) {
      injectVoiceRecognitionService2(voiceRecognitionService);
    }

    @CanIgnoreReturnValue
    private EmergencyHandlerService injectEmergencyHandlerService2(
        EmergencyHandlerService instance) {
      EmergencyHandlerService_MembersInjector.injectEngine(instance, singletonCImpl.provideSafeWordEngineProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private SafeWordPeerService injectSafeWordPeerService2(SafeWordPeerService instance) {
      SafeWordPeerService_MembersInjector.injectPeerBridge(instance, singletonCImpl.providePeerBridgeProvider.get());
      SafeWordPeerService_MembersInjector.injectNotificationHelper(instance, singletonCImpl.provideNotificationHelperProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private VoiceRecognitionService injectVoiceRecognitionService2(
        VoiceRecognitionService instance) {
      VoiceRecognitionService_MembersInjector.injectEngine(instance, singletonCImpl.provideSafeWordEngineProvider.get());
      VoiceRecognitionService_MembersInjector.injectNotificationHelper(instance, singletonCImpl.provideNotificationHelperProvider.get());
      return instance;
    }
  }

  private static final class SingletonCImpl extends SafeWordApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<DataStore<Preferences>> provideDataStoreProvider;

    private Provider<SettingsGateway> provideSettingsGatewayProvider;

    private Provider<SafeWordDatabase> provideDatabaseProvider;

    private Provider<ContactRepository> provideContactRepositoryProvider;

    private Provider<AlertEventRepository> provideAlertEventRepositoryProvider;

    private Provider<NotificationHelper> provideNotificationHelperProvider;

    private Provider<RingerManager> provideRingerManagerProvider;

    private Provider<SoundPlayer> provideSoundPlayerProvider;

    private Provider<SmsSender> provideSmsSenderProvider;

    private Provider<LocationHelper> provideLocationHelperProvider;

    private Provider<EmergencyDispatcher> provideEmergencyDispatcherProvider;

    private Provider<CoroutineScope> provideAppScopeProvider;

    private Provider<PeerBridge> providePeerBridgeProvider;

    private Provider<TimeProvider> provideTimeProvider;

    private Provider<SafeWordEngine> provideSafeWordEngineProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private ContactDao contactDao() {
      return DatabaseModule_ProvideContactDaoFactory.provideContactDao(provideDatabaseProvider.get());
    }

    private AlertEventDao alertEventDao() {
      return DatabaseModule_ProvideAlertEventDaoFactory.provideAlertEventDao(provideDatabaseProvider.get());
    }

    private UpsertContactUseCase upsertContactUseCase() {
      return DomainModule_ProvideUpsertContactUseCaseFactory.provideUpsertContactUseCase(provideContactRepositoryProvider.get(), provideTimeProvider.get());
    }

    private DeleteContactUseCase deleteContactUseCase() {
      return DomainModule_ProvideDeleteContactUseCaseFactory.provideDeleteContactUseCase(provideContactRepositoryProvider.get());
    }

    private ToggleListeningUseCase toggleListeningUseCase() {
      return DomainModule_ProvideToggleListeningUseCaseFactory.provideToggleListeningUseCase(provideSettingsGatewayProvider.get());
    }

    private ToggleIncomingModeUseCase toggleIncomingModeUseCase() {
      return DomainModule_ProvideToggleIncomingModeUseCaseFactory.provideToggleIncomingModeUseCase(provideSettingsGatewayProvider.get());
    }

    private ToggleTestModeUseCase toggleTestModeUseCase() {
      return DomainModule_ProvideToggleTestModeUseCaseFactory.provideToggleTestModeUseCase(provideSettingsGatewayProvider.get());
    }

    private UpdateSafeWordsUseCase updateSafeWordsUseCase() {
      return DomainModule_ProvideUpdateSafeWordsUseCaseFactory.provideUpdateSafeWordsUseCase(provideSettingsGatewayProvider.get());
    }

    private UpdateSensitivityUseCase updateSensitivityUseCase() {
      return DomainModule_ProvideUpdateSensitivityUseCaseFactory.provideUpdateSensitivityUseCase(provideSettingsGatewayProvider.get());
    }

    private ToggleIncludeLocationUseCase toggleIncludeLocationUseCase() {
      return DomainModule_ProvideToggleIncludeLocationUseCaseFactory.provideToggleIncludeLocationUseCase(provideSettingsGatewayProvider.get());
    }

    private TogglePlaySirenUseCase togglePlaySirenUseCase() {
      return DomainModule_ProvideTogglePlaySirenUseCaseFactory.provideTogglePlaySirenUseCase(provideSettingsGatewayProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<DataStore<Preferences>>(singletonCImpl, 2));
      this.provideSettingsGatewayProvider = DoubleCheck.provider(new SwitchingProvider<SettingsGateway>(singletonCImpl, 1));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<SafeWordDatabase>(singletonCImpl, 4));
      this.provideContactRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ContactRepository>(singletonCImpl, 3));
      this.provideAlertEventRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AlertEventRepository>(singletonCImpl, 5));
      this.provideNotificationHelperProvider = DoubleCheck.provider(new SwitchingProvider<NotificationHelper>(singletonCImpl, 7));
      this.provideRingerManagerProvider = DoubleCheck.provider(new SwitchingProvider<RingerManager>(singletonCImpl, 8));
      this.provideSoundPlayerProvider = DoubleCheck.provider(new SwitchingProvider<SoundPlayer>(singletonCImpl, 9));
      this.provideSmsSenderProvider = DoubleCheck.provider(new SwitchingProvider<SmsSender>(singletonCImpl, 10));
      this.provideLocationHelperProvider = DoubleCheck.provider(new SwitchingProvider<LocationHelper>(singletonCImpl, 11));
      this.provideEmergencyDispatcherProvider = DoubleCheck.provider(new SwitchingProvider<EmergencyDispatcher>(singletonCImpl, 6));
      this.provideAppScopeProvider = DoubleCheck.provider(new SwitchingProvider<CoroutineScope>(singletonCImpl, 13));
      this.providePeerBridgeProvider = DoubleCheck.provider(new SwitchingProvider<PeerBridge>(singletonCImpl, 12));
      this.provideTimeProvider = DoubleCheck.provider(new SwitchingProvider<TimeProvider>(singletonCImpl, 14));
      this.provideSafeWordEngineProvider = DoubleCheck.provider(new SwitchingProvider<SafeWordEngine>(singletonCImpl, 0));
    }

    @Override
    public void injectSafeWordApp(SafeWordApp safeWordApp) {
    }

    @Override
    public void injectSafeWordSmsReceiver(SafeWordSmsReceiver safeWordSmsReceiver) {
      injectSafeWordSmsReceiver2(safeWordSmsReceiver);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @CanIgnoreReturnValue
    private SafeWordSmsReceiver injectSafeWordSmsReceiver2(SafeWordSmsReceiver instance) {
      SafeWordSmsReceiver_MembersInjector.injectEngine(instance, provideSafeWordEngineProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.safeword.shared.domain.SafeWordEngine 
          return (T) DomainModule_ProvideSafeWordEngineFactory.provideSafeWordEngine(singletonCImpl.provideSettingsGatewayProvider.get(), singletonCImpl.provideContactRepositoryProvider.get(), singletonCImpl.provideAlertEventRepositoryProvider.get(), singletonCImpl.provideEmergencyDispatcherProvider.get(), singletonCImpl.providePeerBridgeProvider.get(), singletonCImpl.provideTimeProvider.get(), singletonCImpl.provideAppScopeProvider.get());

          case 1: // com.safeword.shared.domain.repository.SettingsGateway 
          return (T) GatewayModule_ProvideSettingsGatewayFactory.provideSettingsGateway(singletonCImpl.provideDataStoreProvider.get());

          case 2: // androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> 
          return (T) GatewayModule_ProvideDataStoreFactory.provideDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.safeword.shared.domain.repository.ContactRepository 
          return (T) GatewayModule_ProvideContactRepositoryFactory.provideContactRepository(singletonCImpl.contactDao());

          case 4: // com.safeword.data.db.SafeWordDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.safeword.shared.domain.repository.AlertEventRepository 
          return (T) GatewayModule_ProvideAlertEventRepositoryFactory.provideAlertEventRepository(singletonCImpl.alertEventDao());

          case 6: // com.safeword.shared.domain.service.EmergencyDispatcher 
          return (T) DomainModule_ProvideEmergencyDispatcherFactory.provideEmergencyDispatcher(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideNotificationHelperProvider.get(), singletonCImpl.provideRingerManagerProvider.get(), singletonCImpl.provideSoundPlayerProvider.get(), singletonCImpl.provideSmsSenderProvider.get(), singletonCImpl.provideLocationHelperProvider.get());

          case 7: // com.safeword.util.NotificationHelper 
          return (T) DomainModule_ProvideNotificationHelperFactory.provideNotificationHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 8: // com.safeword.util.RingerManager 
          return (T) DomainModule_ProvideRingerManagerFactory.provideRingerManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.safeword.util.SoundPlayer 
          return (T) DomainModule_ProvideSoundPlayerFactory.provideSoundPlayer(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 10: // com.safeword.util.SmsSender 
          return (T) DomainModule_ProvideSmsSenderFactory.provideSmsSender();

          case 11: // com.safeword.util.LocationHelper 
          return (T) DomainModule_ProvideLocationHelperFactory.provideLocationHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 12: // com.safeword.shared.bridge.PeerBridge 
          return (T) DomainModule_ProvidePeerBridgeFactory.providePeerBridge(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideAppScopeProvider.get());

          case 13: // kotlinx.coroutines.CoroutineScope 
          return (T) DomainModule_ProvideAppScopeFactory.provideAppScope();

          case 14: // com.safeword.shared.util.TimeProvider 
          return (T) DomainModule_ProvideTimeProviderFactory.provideTimeProvider();

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
