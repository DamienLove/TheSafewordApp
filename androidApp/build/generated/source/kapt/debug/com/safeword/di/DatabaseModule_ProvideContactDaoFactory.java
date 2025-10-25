package com.safeword.di;

import com.safeword.data.db.ContactDao;
import com.safeword.data.db.SafeWordDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvideContactDaoFactory implements Factory<ContactDao> {
  private final Provider<SafeWordDatabase> dbProvider;

  public DatabaseModule_ProvideContactDaoFactory(Provider<SafeWordDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ContactDao get() {
    return provideContactDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideContactDaoFactory create(
      Provider<SafeWordDatabase> dbProvider) {
    return new DatabaseModule_ProvideContactDaoFactory(dbProvider);
  }

  public static ContactDao provideContactDao(SafeWordDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideContactDao(db));
  }
}
