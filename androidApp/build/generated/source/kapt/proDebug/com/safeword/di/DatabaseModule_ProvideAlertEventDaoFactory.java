package com.safeword.di;

import com.safeword.data.db.AlertEventDao;
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
public final class DatabaseModule_ProvideAlertEventDaoFactory implements Factory<AlertEventDao> {
  private final Provider<SafeWordDatabase> dbProvider;

  public DatabaseModule_ProvideAlertEventDaoFactory(Provider<SafeWordDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AlertEventDao get() {
    return provideAlertEventDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideAlertEventDaoFactory create(
      Provider<SafeWordDatabase> dbProvider) {
    return new DatabaseModule_ProvideAlertEventDaoFactory(dbProvider);
  }

  public static AlertEventDao provideAlertEventDao(SafeWordDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAlertEventDao(db));
  }
}
