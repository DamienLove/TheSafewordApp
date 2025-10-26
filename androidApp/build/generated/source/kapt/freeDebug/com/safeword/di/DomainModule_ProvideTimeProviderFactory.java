package com.safeword.di;

import com.safeword.shared.util.TimeProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class DomainModule_ProvideTimeProviderFactory implements Factory<TimeProvider> {
  @Override
  public TimeProvider get() {
    return provideTimeProvider();
  }

  public static DomainModule_ProvideTimeProviderFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TimeProvider provideTimeProvider() {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideTimeProvider());
  }

  private static final class InstanceHolder {
    private static final DomainModule_ProvideTimeProviderFactory INSTANCE = new DomainModule_ProvideTimeProviderFactory();
  }
}
