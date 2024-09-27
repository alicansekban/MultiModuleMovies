package com.alican.multimodulemovies.components.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alican.multimodulemovies.ui.home.HomeScreen
import com.alican.multimodulemovies.utils.HomeScreenRoute
import com.alican.multimodulemovies.utils.SavedScreenRoute
import com.alican.multimodulemovies.utils.SearchScreenRoute

@Composable
fun MainNavigation(
    navController: NavHostController,
    modifier: Modifier,
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = HomeScreenRoute,
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
       composable<HomeScreenRoute> {
           HomeScreen()
       }
        composable<SearchScreenRoute> {
            HomeScreen()
        }
        composable<SavedScreenRoute> {
            HomeScreen()
        }
    }

}

