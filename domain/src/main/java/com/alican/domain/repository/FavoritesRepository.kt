package com.alican.domain.repository

import com.alican.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteMovies(): Flow<List<Movie>>
    fun getFavoriteMovieIds(): Flow<List<Int>>
    fun isMovieFavorite(movieId: Int): Flow<Boolean>
    suspend fun addToFavorites(movie: Movie)
    suspend fun removeFromFavorites(movieId: Int)
    fun getFavoritesCount(): Flow<Int>
}
