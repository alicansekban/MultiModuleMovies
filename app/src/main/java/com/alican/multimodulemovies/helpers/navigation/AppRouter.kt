package com.alican.multimodulemovies.helpers.navigation

import androidx.navigation3.runtime.NavKey
import com.alican.multimodulemovies.helpers.navigation3.BottomNavRoutes
import com.alican.multimodulemovies.helpers.navigation3.Navigator
import javax.inject.Inject
import javax.inject.Singleton

interface AppRouter {
    fun setNavigator(navigator: Navigator)

    // Generic navigation method
    fun navigateTo(route: NavKey)

    // Common navigation patterns
    fun navigateBack()
    fun navigateAndClearBackStack(route: NavKey)
    fun navigateToBottomBarTab(route: BottomNavRoutes)
}

@Singleton
class AppRouterImpl @Inject constructor() : AppRouter {
    private var navigator: Navigator? = null

    override fun setNavigator(navigator: Navigator) {
        this.navigator = navigator
    }

    override fun navigateTo(route: NavKey) {
        navigator?.navigate(route)
    }

    override fun navigateBack() {
        navigator?.goBack()
    }

    override fun navigateAndClearBackStack(route: NavKey) {
        navigator?.let { nav ->
            // For clearing back stack, we navigate to a top-level route
            if (route is BottomNavRoutes) {
                nav.state.topLevelRoute = route
                // Clear the current back stack
                nav.state.backStacks[route]?.clear()
                nav.state.backStacks[route]?.add(route)
            } else {
                // For non-bottom bar routes, navigate normally
                nav.navigate(route)
            }
        }
    }

    override fun navigateToBottomBarTab(route: BottomNavRoutes) {
        navigator?.let { nav ->
            nav.state.topLevelRoute = route
        }
    }
}