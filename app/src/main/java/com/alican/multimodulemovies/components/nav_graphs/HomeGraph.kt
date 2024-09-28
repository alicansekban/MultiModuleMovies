package com.alican.multimodulemovies.components.nav_graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alican.multimodulemovies.ui.home.HomeScreen
import com.alican.multimodulemovies.ui.list.MoviesListScreen
import com.alican.multimodulemovies.utils.HomeHost
import com.alican.multimodulemovies.utils.HomeScreenRoute
import com.alican.multimodulemovies.utils.MoviesListRoute

fun NavGraphBuilder.homeGraph(navController: NavController) {

    navigation<HomeHost>(
        startDestination = HomeScreenRoute,
    ) {
        composable<HomeScreenRoute> {
            HomeScreen(
                openListScreen = {
                    val route = MoviesListRoute(
                        movieType = it
                    )
                    navController.navigate(
                        route = route
                    )
                }
            )
        }

        composable<MoviesListRoute> {
            MoviesListScreen()
        }

    }
}