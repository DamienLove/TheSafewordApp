package com.safeword.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.safeword.data.db.SafeWordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): SafeWordDatabase = Room.databaseBuilder(
        context,
        SafeWordDatabase::class.java,
        "safeword.db"
    )
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
        .build()

    @Provides
    fun provideContactDao(db: SafeWordDatabase) = db.contactDao()

    @Provides
    fun provideAlertEventDao(db: SafeWordDatabase) = db.alertEventDao()
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE contacts ADD COLUMN safewordPeer INTEGER NOT NULL DEFAULT 0")
    }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE contacts ADD COLUMN planTier TEXT")
    }
}

private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE contacts ADD COLUMN linkStatus TEXT NOT NULL DEFAULT 'UNLINKED'")
        database.execSQL("UPDATE contacts SET linkStatus = 'LINKED' WHERE safewordPeer = 1")
    }
}

