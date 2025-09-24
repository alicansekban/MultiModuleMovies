package com.alican.multimodulemovies.di

import android.content.Context
import com.alican.multimodulemovies.helpers.data_store.AppDataStore
import com.alican.multimodulemovies.helpers.data_store.DataStoreManager
import com.alican.multimodulemovies.helpers.notification.AppNotificationManager
import com.alican.multimodulemovies.helpers.security.SecurityManager
import com.alican.multimodulemovies.helpers.theme.ThemeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDataStoreManager(context: Context) = DataStoreManager(context)

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun provideAppDataStore(dataStoreManager: DataStoreManager) = AppDataStore(dataStoreManager)

    @Provides
    @Singleton
    fun provideThemeManager(appDataStore: AppDataStore, context: Context): ThemeManager =
        ThemeManager(appDataStore, context)

    @Provides
    @Singleton
    fun provideNotificationManager(appDataStore: AppDataStore, context: Context) =
        AppNotificationManager(appDataStore = appDataStore, context = context)

    @Provides
    @Singleton
    fun provideSecurityManager(context: Context) =
        SecurityManager(context = context)
}
