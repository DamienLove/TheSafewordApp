package com.safeword.di;

import com.safeword.data.db.ContactDao;
import com.safeword.shared.domain.repository.ContactRepository;
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
public final class GatewayModule_ProvideContactRepositoryFactory implements Factory<ContactRepository> {
  private final Provider<ContactDao> daoProvider;

  public GatewayModule_ProvideContactRepositoryFactory(Provider<ContactDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ContactRepository get() {
    return provideContactRepository(daoProvider.get());
  }

  public static GatewayModule_ProvideContactRepositoryFactory create(
      Provider<ContactDao> daoProvider) {
    return new GatewayModule_ProvideContactRepositoryFactory(daoProvider);
  }

  public static ContactRepository provideContactRepository(ContactDao dao) {
    return Preconditions.checkNotNullFromProvides(GatewayModule.INSTANCE.provideContactRepository(dao));
  }
}
