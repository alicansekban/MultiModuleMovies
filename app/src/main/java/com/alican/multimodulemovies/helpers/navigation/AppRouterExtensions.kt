package com.alican.multimodulemovies.helpers.navigation


import com.alican.domain.models.MovieType
import com.alican.multimodulemovies.navigation.AppRouter
import com.alican.multimodulemovies.utils.ScreenRoute

// Extension functions for common navigation patterns
fun AppRouter.navigateToMovieDetail(movieId: Int) {
    navigateTo(ScreenRoute.MovieDetailRoute(movieId))
}

fun AppRouter.navigateToMoviesList(movieType: MovieType) {
    navigateTo(ScreenRoute.MoviesListRoute(movieType))
}

fun AppRouter.navigateToHome() {
    navigateTo(ScreenRoute.HomeHost)
}

fun AppRouter.navigateToSearch() {
    navigateTo(ScreenRoute.SearchHost)
}

fun AppRouter.navigateToFavorites() {
    navigateTo(ScreenRoute.FavoritesHost)
}

fun AppRouter.navigateToProfile() {
    navigateTo(ScreenRoute.ProfileHost)
}