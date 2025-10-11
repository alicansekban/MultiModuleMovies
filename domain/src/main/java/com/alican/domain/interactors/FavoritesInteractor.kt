package com.alican.domain.interactors

import com.alican.domain.models.Movie
import com.alican.domain.repository.FavoritesRepository
import com.alican.domain.ui_models.movie.MovieUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesInteractor @Inject constructor(
    private val repository: FavoritesRepository
) {

    fun getFavoriteMovies(): Flow<List<MovieUIModel>> {
        return repository.getFavoriteMovies().map { entities ->
            entities.map { entity ->
                MovieUIModel(
                    id = entity.id,
                    title = entity.title,
                    imageUrl = entity.posterPath,
                    overview = entity.overview,
                    isFavorite = true
                )
            }
        }
    }

    fun getFavoriteMovieIds(): Flow<List<Int>> {
        return repository.getFavoriteMovieIds()
    }

    fun isMovieFavorite(movieId: Int): Flow<Boolean> {
        return repository.isMovieFavorite(movieId)
    }

    suspend fun addToFavorites(movie: MovieUIModel) {
        val entity = Movie(
            title = movie.title,
            id = movie.id,
            posterPath = movie.imageUrl,
            overview = movie.overview
        )
        repository.addToFavorites(entity)
    }

    suspend fun removeFromFavorites(movieId: Int) {
        repository.removeFromFavorites(movieId)
    }

    suspend fun toggleFavorite(movie: MovieUIModel) {
        val movieId = movie.id
        if (movie.isFavorite) {
            removeFromFavorites(movieId)
        } else {
            addToFavorites(movie)
        }
    }

    fun getFavoritesCount(): Flow<Int> {
        return repository.getFavoritesCount()
    }
}