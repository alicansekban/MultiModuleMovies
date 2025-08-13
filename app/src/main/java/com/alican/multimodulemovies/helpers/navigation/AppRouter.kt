package com.alican.multimodulemovies.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.alican.multimodulemovies.utils.ScreenRoute
import javax.inject.Inject
import javax.inject.Singleton

interface AppRouter {
    fun setNavController(navController: NavController)

    // Generic navigation method
    fun navigateTo(
        route: ScreenRoute,
        builder: (NavOptionsBuilder.() -> Unit)? = null
    )

    // Common navigation patterns
    fun navigateBack(): Boolean
    fun navigateAndClearBackStack(route: ScreenRoute)
    fun navigateAndPopUpTo(
        route: ScreenRoute,
        popUpToRoute: ScreenRoute,
        inclusive: Boolean = false
    )
}

@Singleton
class AppRouterImpl @Inject constructor() : AppRouter {
    private var navController: NavController? = null

    override fun setNavController(navController: NavController) {
        this.navController = navController
    }

    override fun navigateTo(
        route: ScreenRoute,
        builder: (NavOptionsBuilder.() -> Unit)?
    ) {
        navController?.navigate(route) {
            builder?.invoke(this)
        }
    }

    override fun navigateBack(): Boolean {
        return navController?.popBackStack() ?: false
    }

    override fun navigateAndClearBackStack(route: ScreenRoute) {
        navigateTo(route) {
            popUpTo(navController?.graph?.startDestinationId ?: 0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    override fun navigateAndPopUpTo(
        route: ScreenRoute,
        popUpToRoute: ScreenRoute,
        inclusive: Boolean
    ) {
        navigateTo(route) {
            popUpTo(popUpToRoute) {
                this.inclusive = inclusive
            }
        }
    }
}