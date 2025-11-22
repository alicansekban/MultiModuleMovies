@file:JvmName("NavigationEntriesKt")

package com.alican.multimodulemovies.helpers.navigation3.entry

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.alican.multimodulemovies.helpers.navigation3.router.AppRouter
import com.alican.multimodulemovies.ui.about_us.AboutScreen
import com.alican.multimodulemovies.ui.favorites.FavoritesScreen
import com.alican.multimodulemovies.ui.help.HelpSupportScreen
import com.alican.multimodulemovies.ui.home.HomeScreen
import com.alican.multimodulemovies.ui.privacy.PrivacyPolicyScreen
import com.alican.multimodulemovies.ui.profile.ProfileScreen
import com.alican.multimodulemovies.ui.search.SearchScreen


@Composable
fun EntryProviderScope<NavKey>.ProfileEntry(
    appRouter: AppRouter
) {
    entry<BottomNavRoutes.Profile> {
        ProfileScreen()
    }

    entry<EntryRoutes.AboutEntryRoutes> {
        AboutScreen(
            onBackClicked = {
                appRouter.navigateBack()
            }
        )
    }

    entry<EntryRoutes.HelpEntryRoutes>(
        metadata =
            NavDisplay.transitionSpec {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(1000)
                ) togetherWith ExitTransition.KeepUntilTransitionsFinished
            } + NavDisplay.popTransitionSpec {
                EnterTransition.None togetherWith slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(1000)
                )
            }
    ) {
        HelpSupportScreen(
            onBackClicked = {
                appRouter.navigateBack()
            }
        )
    }

    entry<EntryRoutes.PrivacyPolicyEntryRoutes> {
        PrivacyPolicyScreen(
            onBackClicked = {
                appRouter.navigateBack()
            }
        )
    }
}

@Composable
fun EntryProviderScope<NavKey>.FavoritesEntry(
    appRouter: AppRouter
) {
    entry<BottomNavRoutes.Favorites> {
        FavoritesScreen()
    }
}

@Composable
fun EntryProviderScope<NavKey>.HomeEntry(
    appRouter: AppRouter
) {
    entry<BottomNavRoutes.Home> {
        HomeScreen()
    }
}


@Composable
fun EntryProviderScope<NavKey>.SearchEntry(
    appRouter: AppRouter
) {

    entry<BottomNavRoutes.Search> {
        SearchScreen()
    }
}