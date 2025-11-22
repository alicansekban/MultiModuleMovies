package com.alican.multimodulemovies.helpers.navigation3

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationStateProvider @Inject constructor() {
    private var _navigationState: NavigationState? = null

    val navigationState: NavigationState
        get() = _navigationState
            ?: throw IllegalStateException("NavigationState not initialized. Call initialize() first.")

    fun initialize(navigationState: NavigationState) {
        _navigationState = navigationState
    }

    fun isInitialized(): Boolean = _navigationState != null
}