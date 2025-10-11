package com.alican.multimodulemovies.di

import com.alican.data.auth.UserAuthManager
import com.alican.data.data.repository.FavoritesRepositoryImpl
import com.alican.data.data.repository.MoviesRepositoryImpl
import com.alican.domain.repository.FavoritesRepository
import com.alican.domain.repository.MoviesRepository
import com.alican.domain.repository.UserAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMoviesRepository(
        moviesRepositoryImpl: MoviesRepositoryImpl
    ): MoviesRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindUserAuthRepository(
        userAuthManager: UserAuthManager
    ): UserAuthRepository
}