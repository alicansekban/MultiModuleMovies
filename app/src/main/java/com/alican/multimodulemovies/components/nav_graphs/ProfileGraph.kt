package com.alican.multimodulemovies.components.nav_graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.alican.multimodulemovies.ui.about_us.AboutScreen
import com.alican.multimodulemovies.ui.help.HelpSupportScreen
import com.alican.multimodulemovies.ui.profile.ProfileScreen
import com.alican.multimodulemovies.utils.ScreenRoute

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation<ScreenRoute.ProfileHost>(
        startDestination = ScreenRoute.ProfileScreenRoute,
    ) {
        composable<ScreenRoute.ProfileScreenRoute> {
            ProfileScreen()
        }

        composable<ScreenRoute.AboutScreenRoute> {
            AboutScreen()
        }

        composable<ScreenRoute.HelpScreenRoute> {
            HelpSupportScreen()
        }
    }
}