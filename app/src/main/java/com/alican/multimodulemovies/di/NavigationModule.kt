package com.alican.multimodulemovies.di

import com.alican.multimodulemovies.helpers.navigation3.router.AppRouter
import com.alican.multimodulemovies.helpers.navigation3.router.AppRouterImpl
import dagger.Binds
import dagger.Module
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
}