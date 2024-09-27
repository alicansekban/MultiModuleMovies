package com.alican.multimodulemovies.components.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.alican.multimodulemovies.components.nav_graphs.homeGraph
import com.alican.multimodulemovies.components.nav_graphs.favoritesGraph
import com.alican.multimodulemovies.components.nav_graphs.searchGraph
import com.alican.multimodulemovies.utils.HomeHost

@Composable
fun MainNavigation(
    navController: NavHostController,
    modifier: Modifier,
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = HomeHost,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                animationSpec = tween(200, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Left
            )
        },
        exitTransition = {
            slideOutOfContainer(
                animationSpec = tween(200, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Left
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                animationSpec = tween(200, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Right
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                animationSpec = tween(200, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Right
            )
        }

    ) {
        homeGraph(navController = navController)
        searchGraph(navController = navController)
        favoritesGraph(navController = navController)

    }

}

