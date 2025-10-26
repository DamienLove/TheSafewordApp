package com.safeword.di;

import android.content.Context;
import com.safeword.util.NotificationHelper;
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
public final class DomainModule_ProvideNotificationHelperFactory implements Factory<NotificationHelper> {
  private final Provider<Context> contextProvider;

  public DomainModule_ProvideNotificationHelperFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public NotificationHelper get() {
    return provideNotificationHelper(contextProvider.get());
  }

  public static DomainModule_ProvideNotificationHelperFactory create(
      Provider<Context> contextProvider) {
    return new DomainModule_ProvideNotificationHelperFactory(contextProvider);
  }

  public static NotificationHelper provideNotificationHelper(Context context) {
    return Preconditions.checkNotNullFromProvides(DomainModule.INSTANCE.provideNotificationHelper(context));
  }
}
