package com.alican.multimodulemovies.utils

import com.alican.domain.models.MovieType
import kotlinx.serialization.Serializable

@Serializable
object HomeScreenRoute

@Serializable
object SearchScreenRoute

@Serializable
object FavoritesScreenRoute


@Serializable
object HomeHost

@Serializable
object SearchHost

@Serializable
object FavoritesHost

@Serializable
data class MoviesListRoute(
    val movieType: MovieType = MovieType.UPCOMING
)

@Serializable
data class MovieDetailRoute(
    val movieId: Int
)
