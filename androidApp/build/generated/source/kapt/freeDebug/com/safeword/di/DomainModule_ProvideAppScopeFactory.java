package com.safeword.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineScope;

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
public final class DomainModule_ProvideAppScopeFactory implements Factory<CoroutineScope> {
  @Override
  public CoroutineScope get() {
    return provideAppScope();
  }

  public static DomainModule_ProvideAppScopeFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CoroutineScope provideAppScope() {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideAppScope());
  }

  private static final class InstanceHolder {
    private static final DomainModule_ProvideAppScopeFactory INSTANCE = new DomainModule_ProvideAppScopeFactory();
  }
}
