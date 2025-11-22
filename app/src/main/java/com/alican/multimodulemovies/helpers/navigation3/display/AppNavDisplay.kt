@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.alican.multimodulemovies.helpers.navigation3.display

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.alican.multimodulemovies.helpers.navigation3.router.AppRouter


@Composable
fun AppNavDisplay(
    modifier: Modifier = Modifier,
    appRouter: AppRouter,
    entries: List<NavEntry<NavKey>>,
) {
    val strategy = rememberListDetailSceneStrategy<NavKey>()

    NavDisplay(
        sceneStrategy = strategy,
        modifier = modifier,
        onBack = { appRouter.navigateBack() },
        entries = entries,
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it }
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it }
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it }
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it }
            )
        },
        /**
         * comment out and add your animation if you want to customize predictive pop transition
         */
//        predictivePopTransitionSpec = {
//            slideInHorizontally(
//                initialOffsetX = { -it }
//            ) togetherWith slideOutHorizontally(
//                targetOffsetX = { it }
//            )
//        }
    )
}