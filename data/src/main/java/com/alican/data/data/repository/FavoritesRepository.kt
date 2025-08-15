package com.alican.data.data.repository

import com.alican.data.data.local.dao.MoviesDao
import com.alican.data.data.local.entity.MoviesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val moviesDao: MoviesDao
) {

    fun getFavoriteMoviesEntities(): Flow<List<MoviesEntity>> = moviesDao.getFavoriteMovies()
    fun getFavoriteMovieIdsEntities(): Flow<List<Int>> = moviesDao.getFavoriteMovieIds()
    fun isMovieFavoriteEntity(movieId: Int): Flow<Boolean> = moviesDao.isMovieFavorite(movieId)
    suspend fun addToFavoritesEntity(movie: MoviesEntity) = moviesDao.addToFavorites(movie)
    suspend fun removeFromFavoritesEntity(movieId: Int) = moviesDao.removeFromFavorites(movieId)
    fun getFavoritesCountEntity(): Flow<Int> = moviesDao.getFavoritesCount()

}