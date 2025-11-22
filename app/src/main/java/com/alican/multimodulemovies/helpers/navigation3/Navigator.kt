
package com.alican.multimodulemovies.helpers.navigation3

import androidx.navigation3.runtime.NavKey
import com.alican.multimodulemovies.helpers.navigation3.entry.BottomNavRoutes
import com.alican.multimodulemovies.helpers.navigation3.state.NavigationState
import com.alican.multimodulemovies.helpers.navigation3.state.NavigationStateProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(
    private val navigationStateProvider: NavigationStateProvider
) {
    val state: NavigationState
        get() = navigationStateProvider.navigationState

    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute] ?: return
        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
    fun navigateAndClearBackStack(route: NavKey) {
        // For clearing back stack, we navigate to a top-level route
        if (route is BottomNavRoutes) {
            state.topLevelRoute = route
            // Clear the current back stack
            state.backStacks[route]?.clear()
            state.backStacks[route]?.add(route)
        } else {
            // For non-bottom bar routes, navigate normally
            navigate(route)
        }

    }
}