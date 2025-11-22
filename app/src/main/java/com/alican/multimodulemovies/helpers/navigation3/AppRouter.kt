package com.alican.multimodulemovies.helpers.navigation3

import androidx.navigation3.runtime.NavKey
import javax.inject.Inject
import javax.inject.Singleton

interface AppRouter {
    // Generic navigation method
    fun navigateTo(route: NavKey)

    // Common navigation patterns
    fun navigateBack()
    fun navigateAndClearBackStack(route: NavKey)
    fun navigateToBottomBarTab(route: BottomNavRoutes)
}

@Singleton
class AppRouterImpl @Inject constructor(
    private val navigator: Navigator,
    private val navigationStateProvider: NavigationStateProvider
) : AppRouter {

    override fun navigateTo(route: NavKey) {
        if (navigationStateProvider.isInitialized()) {
            navigator.navigate(route)
        }
    }

    override fun navigateBack() {
        if (navigationStateProvider.isInitialized()) {
            navigator.goBack()
        }
    }

    override fun navigateAndClearBackStack(route: NavKey) {
        if (navigationStateProvider.isInitialized()) {
            val nav = navigator
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
        if (navigationStateProvider.isInitialized()) {
            navigator.state.topLevelRoute = route
        }
    }
}