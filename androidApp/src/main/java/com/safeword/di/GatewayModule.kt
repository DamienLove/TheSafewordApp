package com.safeword.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.safeword.data.AlertEventRepositoryImpl
import com.safeword.data.ContactRepositoryImpl
import com.safeword.data.db.AlertEventDao
import com.safeword.data.db.ContactDao
import com.safeword.data.prefs.SettingsGatewayImpl
import com.safeword.data.prefs.safeWordDataStore
import com.safeword.shared.domain.repository.AlertEventRepository
import com.safeword.shared.domain.repository.ContactRepository
import com.safeword.shared.domain.repository.SettingsGateway
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GatewayModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.safeWordDataStore

    @Provides
    @Singleton
    fun provideSettingsGateway(dataStore: DataStore<Preferences>): SettingsGateway =
        SettingsGatewayImpl(dataStore)

    @Provides
    @Singleton
    fun provideContactRepository(dao: ContactDao): ContactRepository =
        ContactRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideAlertEventRepository(dao: AlertEventDao): AlertEventRepository =
        AlertEventRepositoryImpl(dao)
}

