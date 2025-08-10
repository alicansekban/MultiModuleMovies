package com.alican.multimodulemovies.components.nav_graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alican.multimodulemovies.ui.search.SearchScreen
import com.alican.multimodulemovies.utils.ScreenRoute

fun NavGraphBuilder.searchGraph(navController: NavController) {

    navigation<ScreenRoute.SearchHost>(
        startDestination = ScreenRoute.SearchScreenRoute,
    ) {
        composable<ScreenRoute.SearchScreenRoute> {
            SearchScreen()
        }

    }
}