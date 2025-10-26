package com.safeword.di;

import com.safeword.util.SmsSender;
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
public final class DomainModule_ProvideSmsSenderFactory implements Factory<SmsSender> {
  @Override
  public SmsSender get() {
    return provideSmsSender();
  }

  public static DomainModule_ProvideSmsSenderFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SmsSender provideSmsSender() {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideSmsSender());
  }

  private static final class InstanceHolder {
    private static final DomainModule_ProvideSmsSenderFactory INSTANCE = new DomainModule_ProvideSmsSenderFactory();
  }
}
