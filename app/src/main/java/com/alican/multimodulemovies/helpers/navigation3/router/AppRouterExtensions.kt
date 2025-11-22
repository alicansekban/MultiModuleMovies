package com.alican.multimodulemovies.helpers.navigation3.router


import com.alican.domain.ui_models.movie.MovieType
import com.alican.multimodulemovies.helpers.navigation3.entry.EntryRoutes

// Extension functions for common navigation patterns
fun AppRouter.navigateToMovieDetail(movieId: Int) {
    navigateTo(EntryRoutes.MovieDetailRoute(movieId))
}

fun AppRouter.navigateToMoviesList(movieType: MovieType) {
    navigateTo(EntryRoutes.MoviesListRoute(movieType))
}
fun AppRouter.navigateToLogin() {
    navigateTo(EntryRoutes.LoginEntryRoutes)
}

fun AppRouter.navigateToRegister() {
    navigateTo(EntryRoutes.RegisterEntryRoutes)
}

fun AppRouter.navigateToAboutUs() {
    navigateTo(EntryRoutes.AboutEntryRoutes)
}

fun AppRouter.navigateToHelp() {
    navigateTo(EntryRoutes.HelpEntryRoutes)
}

fun AppRouter.navigateToPrivacyPolicy() {
    navigateTo(EntryRoutes.PrivacyPolicyEntryRoutes)
}