package com.safeword.di;

import android.content.Context;
import com.safeword.util.RingerManager;
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
public final class DomainModule_ProvideRingerManagerFactory implements Factory<RingerManager> {
  private final Provider<Context> contextProvider;

  public DomainModule_ProvideRingerManagerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public RingerManager get() {
    return provideRingerManager(contextProvider.get());
  }

  public static DomainModule_ProvideRingerManagerFactory create(Provider<Context> contextProvider) {
    return new DomainModule_ProvideRingerManagerFactory(contextProvider);
  }

  public static RingerManager provideRingerManager(Context context) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideRingerManager(context));
  }
}
