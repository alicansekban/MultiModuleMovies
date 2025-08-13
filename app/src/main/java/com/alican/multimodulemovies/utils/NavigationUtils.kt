package com.alican.multimodulemovies.utils

import com.alican.domain.models.MovieType
import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoute {
    @Serializable
    data object SplashScreen : ScreenRoute()

    @Serializable
    data object HomeHost : ScreenRoute()

    @Serializable
    data object SearchHost : ScreenRoute()

    @Serializable
    data object FavoritesHost : ScreenRoute()

    @Serializable
    data object ProfileHost : ScreenRoute()

    @Serializable
    data object HomeScreenRoute : ScreenRoute()

    @Serializable
    data object SearchScreenRoute : ScreenRoute()

    @Serializable
    data object FavoritesScreenRoute : ScreenRoute()

    @Serializable
    data class MoviesListRoute(
        val movieType: MovieType = MovieType.UPCOMING
    ) : ScreenRoute()

    @Serializable
    data class MovieDetailRoute(
        val movieId: Int
    ) : ScreenRoute()

    @Serializable
    data object ProfileScreenRoute : ScreenRoute()

    @Serializable
    data object LoginScreenRoute : ScreenRoute()

    @Serializable
    data object RegisterScreenRoute : ScreenRoute()

}
