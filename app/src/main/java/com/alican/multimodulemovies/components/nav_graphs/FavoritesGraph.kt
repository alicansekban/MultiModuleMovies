package com.alican.multimodulemovies.components.nav_graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alican.multimodulemovies.ui.favorites.FavoritesScreen
import com.alican.multimodulemovies.utils.ScreenRoute

fun NavGraphBuilder.favoritesGraph(navController: NavController) {

    navigation<ScreenRoute.FavoritesHost>(
        startDestination = ScreenRoute.FavoritesScreenRoute,
    ) {
        composable<ScreenRoute.FavoritesScreenRoute> {
            FavoritesScreen()
        }

    }
}