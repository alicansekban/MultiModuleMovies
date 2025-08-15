package com.alican.domain.interactors

import com.alican.data.data.local.entity.MoviesEntity
import com.alican.data.data.repository.FavoritesRepository
import com.alican.domain.models.MovieUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesInteractor @Inject constructor(
    private val repository: FavoritesRepository
) {

    fun getFavoriteMovies(): Flow<List<MovieUIModel>> {
        return repository.getFavoriteMoviesEntities().map { entities ->
            entities.map { entity ->
                MovieUIModel(
                    id = entity.movieId,
                    title = entity.title,
                    imageUrl = entity.imageUrl,
                    overview = entity.overview,
                    isFavorite = true
                )
            }
        }
    }

    fun getFavoriteMovieIds(): Flow<List<Int>> {
        return repository.getFavoriteMovieIdsEntities()
    }

    fun isMovieFavorite(movieId: Int): Flow<Boolean> {
        return repository.isMovieFavoriteEntity(movieId)
    }

    suspend fun addToFavorites(movie: MovieUIModel) {
        val entity = MoviesEntity(
            movieId = movie.id ?: return,
            title = movie.title ?: "",
            imageUrl = movie.imageUrl,
            overview = movie.overview
        )
        repository.addToFavoritesEntity(entity)
    }

    suspend fun removeFromFavorites(movieId: Int) {
        repository.removeFromFavoritesEntity(movieId)
    }

    suspend fun toggleFavorite(movie: MovieUIModel) {
        val movieId = movie.id ?: return
        if (movie.isFavorite) {
            removeFromFavorites(movieId)
        } else {
            addToFavorites(movie)
        }
    }

    fun getFavoritesCount(): Flow<Int> {
        return repository.getFavoritesCountEntity()
    }
}