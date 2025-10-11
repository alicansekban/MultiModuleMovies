package com.alican.data.data.repository

import com.alican.data.data.local.dao.MoviesDao
import com.alican.data.mappers.toDomainModel
import com.alican.data.mappers.toEntity
import com.alican.domain.models.Movie
import com.alican.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class FavoritesRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao
) : FavoritesRepository {

    override fun getFavoriteMovies(): Flow<List<Movie>> = moviesDao.getFavoriteMovies().map {
        it.map { it.toDomainModel() }
    }

    override fun getFavoriteMovieIds(): Flow<List<Int>> = moviesDao.getFavoriteMovieIds()

    override fun isMovieFavorite(movieId: Int): Flow<Boolean> = moviesDao.isMovieFavorite(movieId)

    override suspend fun addToFavorites(movie: Movie) = moviesDao.addToFavorites(movie.toEntity())

    override suspend fun removeFromFavorites(movieId: Int) = moviesDao.removeFromFavorites(movieId)

    override fun getFavoritesCount(): Flow<Int> = moviesDao.getFavoritesCount()

}
