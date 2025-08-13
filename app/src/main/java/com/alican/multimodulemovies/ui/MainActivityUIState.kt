package com.alican.multimodulemovies.ui


data class MainActivityUIState(
    val isDarkMode: Boolean = false,
    val showThemeDialog: Boolean = false,
    val isNotificationPermissionGranted: Boolean = false,
    val showNotificationPermissionDialog: Boolean = false,
    val hasShownNotificationPermissionDialog: Boolean = false,
    val unreadNotificationCount: Int = 0
)
