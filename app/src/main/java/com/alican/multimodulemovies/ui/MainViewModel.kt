package com.alican.multimodulemovies.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.multimodulemovies.helpers.data_store.AppDataStore
import com.alican.multimodulemovies.helpers.navigation3.AppRouter
import com.alican.multimodulemovies.helpers.navigation3.EntryRoutes
import com.alican.multimodulemovies.helpers.notification.AppNotificationManager
import com.alican.multimodulemovies.helpers.security.SecurityManager
import com.alican.multimodulemovies.helpers.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val themeManager: ThemeManager,
    private val appNotificationManager: AppNotificationManager,
    private val appDataStore: AppDataStore,
    private val securityManager: SecurityManager,
    private val appRouter: AppRouter
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUIState())
    val uiState = _uiState.onStart {
        getTheme()
        observeNotificationSettings()
        checkAppSecurity()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MainActivityUIState()
    )

    companion object {
        private const val TAG = "MainViewModel"
    }

    private fun checkAppSecurity() {
        viewModelScope.launch {
            try {
                val isDeviceNotSecure = securityManager.isDeviceNotSecure()
                if (isDeviceNotSecure) {
                    appRouter.navigateAndClearBackStack(
                        EntryRoutes.SecurityEntryRoutes
                    )
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error checking app security", e)
            }
        }
    }

    private fun getTheme() {
        viewModelScope.launch {
            val isFirstTime = themeManager.isFirstTime()
            if (isFirstTime && themeManager.isSystemDark()) {
                _uiState.update {
                    it.copy(showThemeDialog = true)
                }
            }
            themeManager.observeTheme().collect { isDarkMode ->
                _uiState.update {
                    it.copy(isDarkMode = isDarkMode)
                }
            }
        }
    }

    private fun observeNotificationSettings() {
        viewModelScope.launch {
            try {
                // Combine notification-related flows
                appNotificationManager.unreadCount.collect { unreadCount ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            unreadNotificationCount = unreadCount
                        )
                    }
                }
                // Check if permission dialog was already shown
                val hasShown = appDataStore.hasShownNotificationPermissionDialog()
                _uiState.update {
                    it.copy(hasShownNotificationPermissionDialog = hasShown)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error observing notification settings", e)
            }
        }
    }

    fun setDarkModeFromDialog(isDarkMode: Boolean) {
        viewModelScope.launch {
            themeManager.setDarkMode(isDarkMode)
        }
        _uiState.update {
            it.copy(isDarkMode = isDarkMode, showThemeDialog = false)
        }
    }

    fun updateNotificationPermissionStatus(isGranted: Boolean) {
        viewModelScope.launch {
            try {
                // Save permission status to DataStore
                appDataStore.setNotificationPermissionGranted(isGranted)

                _uiState.update {
                    it.copy(isNotificationPermissionGranted = isGranted)
                }

                Log.d(TAG, "Notification permission status updated: $isGranted")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating notification permission status", e)
            }
        }
    }

    fun showNotificationPermissionDialog() {
        viewModelScope.launch {
            try {
                val hasShown = appDataStore.hasShownNotificationPermissionDialog()
                if (!hasShown) {
                    _uiState.update {
                        it.copy(showNotificationPermissionDialog = true)
                    }

                    // Mark as shown so we don't show it again
                    appDataStore.setHasShownNotificationPermissionDialog(true)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error showing notification permission dialog", e)
            }
        }
    }

    fun dismissNotificationPermissionDialog() {
        _uiState.update {
            it.copy(showNotificationPermissionDialog = false)
        }
    }

    fun onNotificationPermissionGranted() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Notification permission granted - initializing FCM")

                updateNotificationPermissionStatus(true)

                // Enable default notification settings
                val defaultSettings =
                    com.alican.multimodulemovies.helpers.notification.NotificationSettings(
                        pushNotificationsEnabled = true,
                        movieUpdatesEnabled = true,
                        recommendationsEnabled = true,
                        promotionsEnabled = false
                    )

                appNotificationManager.updateNotificationSettings(defaultSettings)

            } catch (e: Exception) {
                Log.e(TAG, "Error handling notification permission granted", e)
            }
        }
    }

    fun onNotificationPermissionDenied() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Notification permission denied")
                updateNotificationPermissionStatus(false)

                // Disable notification-related settings
                appDataStore.setPushNotificationsEnabled(false)

            } catch (e: Exception) {
                Log.e(TAG, "Error handling notification permission denied", e)
            }
        }
    }
}