@file:JvmName("NavigationEntriesKt")

package com.alican.multimodulemovies.helpers.navigation3

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.alican.multimodulemovies.ui.about_us.AboutScreen
import com.alican.multimodulemovies.ui.favorites.FavoritesScreen
import com.alican.multimodulemovies.ui.help.HelpSupportScreen
import com.alican.multimodulemovies.ui.home.HomeScreen
import com.alican.multimodulemovies.ui.privacy.PrivacyPolicyScreen
import com.alican.multimodulemovies.ui.profile.ProfileScreen
import com.alican.multimodulemovies.ui.search.SearchScreen


@Composable
fun EntryProviderScope<NavKey>.ProfileEntry(
    navigator: Navigator
) {
    entry<BottomNavRoutes.Profile> {
        ProfileScreen()
    }

    entry<EntryRoutes.AboutEntryRoutes> {
        AboutScreen()
    }

    entry<EntryRoutes.HelpEntryRoutes> {
        HelpSupportScreen()
    }

    entry<EntryRoutes.PrivacyPolicyEntryRoutes> {
        PrivacyPolicyScreen {
//                navController.navigateUp()
        }
    }
}

@Composable
fun EntryProviderScope<NavKey>.FavoritesEntry(
    navigator: Navigator,
) {
    entry<BottomNavRoutes.Favorites> {
        FavoritesScreen()
    }
}

@Composable
fun EntryProviderScope<NavKey>.HomeEntry(
    navigator: Navigator
) {
    entry<BottomNavRoutes.Home> {
        HomeScreen()
    }
}


@Composable
fun EntryProviderScope<NavKey>.SearchEntry(
    navigator: Navigator
) {

    entry<BottomNavRoutes.Search> {
        SearchScreen()
    }
}