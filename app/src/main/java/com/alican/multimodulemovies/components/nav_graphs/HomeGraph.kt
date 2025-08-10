package com.alican.multimodulemovies.components.nav_graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alican.multimodulemovies.ui.home.HomeScreen
import com.alican.multimodulemovies.ui.list.MoviesListScreen
import com.alican.multimodulemovies.utils.ScreenRoute

fun NavGraphBuilder.homeGraph(navController: NavController) {

    navigation<ScreenRoute.HomeHost>(
        startDestination = ScreenRoute.HomeScreenRoute,
    ) {
        composable<ScreenRoute.HomeScreenRoute> {
            HomeScreen(
                openListScreen = {
                    val route = ScreenRoute.MoviesListRoute(
                        movieType = it
                    )
                    navController.navigate(
                        route = route
                    )
                },
                openMovieDetailScreen = {
                    val route = ScreenRoute.MovieDetailRoute(
                        movieId = it,
                    )
                    navController.navigate(
                        route = route
                    )
                }
            )
        }

        composable<ScreenRoute.MoviesListRoute> {
            MoviesListScreen()
        }
    }
}