package com.alican.multimodulemovies.components.nav_graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alican.multimodulemovies.ui.home.HomeScreen
import com.alican.multimodulemovies.ui.search.SearchScreen
import com.alican.multimodulemovies.utils.HomeHost
import com.alican.multimodulemovies.utils.HomeScreenRoute
import com.alican.multimodulemovies.utils.SearchHost
import com.alican.multimodulemovies.utils.SearchScreenRoute

fun NavGraphBuilder.searchGraph(navController: NavController) {

    navigation<SearchHost>(
        startDestination = SearchScreenRoute,
    ) {
        composable<SearchScreenRoute> {
            SearchScreen()
        }

    }
}