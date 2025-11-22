package com.alican.multimodulemovies.helpers.navigation3.router

import androidx.navigation3.runtime.NavKey
import com.alican.multimodulemovies.helpers.navigation3.Navigator
import com.alican.multimodulemovies.helpers.navigation3.entry.BottomNavRoutes
import com.alican.multimodulemovies.helpers.navigation3.state.NavigationStateProvider
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
            navigator.navigateAndClearBackStack(route)
        }
    }

    override fun navigateToBottomBarTab(route: BottomNavRoutes) {
        if (navigationStateProvider.isInitialized()) {
            navigator.state.topLevelRoute = route
        }
    }
}