package com.alican.multimodulemovies.di

import com.alican.multimodulemovies.helpers.navigation3.AppRouter
import com.alican.multimodulemovies.helpers.navigation3.AppRouterImpl
import com.alican.multimodulemovies.helpers.navigation3.NavigationStateProvider
import com.alican.multimodulemovies.helpers.navigation3.Navigator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindAppRouter(
        appRouterImpl: AppRouterImpl
    ): AppRouter

    companion object {
        @Provides
        @Singleton
        fun provideNavigator(
            navigationStateProvider: NavigationStateProvider
        ): Navigator {
            return Navigator(navigationStateProvider = navigationStateProvider)
        }

        @Provides
        @Singleton
        fun provideNavigationStateProvider(): NavigationStateProvider {
            return NavigationStateProvider()
        }
    }
}