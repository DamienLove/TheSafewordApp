package com.safeword.di;

import com.safeword.data.db.AlertEventDao;
import com.safeword.shared.domain.repository.AlertEventRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class GatewayModule_ProvideAlertEventRepositoryFactory implements Factory<AlertEventRepository> {
  private final Provider<AlertEventDao> daoProvider;

  public GatewayModule_ProvideAlertEventRepositoryFactory(Provider<AlertEventDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public AlertEventRepository get() {
    return provideAlertEventRepository(daoProvider.get());
  }

  public static GatewayModule_ProvideAlertEventRepositoryFactory create(
      Provider<AlertEventDao> daoProvider) {
    return new GatewayModule_ProvideAlertEventRepositoryFactory(daoProvider);
  }

  public static AlertEventRepository provideAlertEventRepository(AlertEventDao dao) {
    return Preconditions.checkNotNullFromProvides(GatewayModule.INSTANCE.provideAlertEventRepository(dao));
  }
}
