package com.alican.multimodulemovies.components.nav_graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alican.multimodulemovies.ui.favorites.FavoritesScreen
import com.alican.multimodulemovies.ui.home.HomeScreen
import com.alican.multimodulemovies.utils.FavoritesHost
import com.alican.multimodulemovies.utils.FavoritesScreenRoute

fun NavGraphBuilder.favoritesGraph(navController: NavController) {

    navigation<FavoritesHost>(
        startDestination = FavoritesScreenRoute,
    ) {
        composable<FavoritesScreenRoute> {
            FavoritesScreen()
        }

    }
}