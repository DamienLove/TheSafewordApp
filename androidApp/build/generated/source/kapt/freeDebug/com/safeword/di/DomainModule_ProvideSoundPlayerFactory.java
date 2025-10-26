package com.safeword.di;

import android.content.Context;
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
public final class DomainModule_ProvideSoundPlayerFactory implements Factory<SoundPlayer> {
  private final Provider<Context> contextProvider;

  public DomainModule_ProvideSoundPlayerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SoundPlayer get() {
    return provideSoundPlayer(contextProvider.get());
  }

  public static DomainModule_ProvideSoundPlayerFactory create(Provider<Context> contextProvider) {
    return new DomainModule_ProvideSoundPlayerFactory(contextProvider);
  }

  public static SoundPlayer provideSoundPlayer(Context context) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideSoundPlayer(context));
  }
}
