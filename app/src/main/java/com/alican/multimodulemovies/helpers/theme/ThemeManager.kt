package com.alican.multimodulemovies.helpers.theme

import android.content.Context
import android.content.res.Configuration
import com.alican.multimodulemovies.helpers.data_store.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ThemeManager @Inject constructor(
    private val appDataStore: AppDataStore,
    private val context: Context
) {

    /**
     * Check if the system is currently in dark mode
     */
    private fun isSystemInDarkMode(): Boolean {
        val currentNightMode =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * Check if this is first time opening the app
     */
    suspend fun isFirstTime(): Boolean {
        return appDataStore.isDarkMode() == null
    }

    /**
     * Get current dark mode state
     * Returns null if first time (no preference saved)
     */
    suspend fun isDarkMode(): Boolean? {
        return appDataStore.isDarkMode()
    }

    /**
     * Set dark mode preference
     */
    suspend fun setDarkMode(isDark: Boolean) {
        appDataStore.setDarkMode(isDark)
    }

    /**
     * Check if system is in dark mode (for first time popup logic)
     */
    fun isSystemDark(): Boolean {
        return isSystemInDarkMode()
    }

    /**
     * Observe theme changes
     */
    fun observeTheme(): Flow<Boolean> = flow {
        var lastValue: Boolean? = null
        while (true) {
            val currentValue = appDataStore.isDarkMode()
            if (currentValue != lastValue && currentValue != null) {
                emit(currentValue)
                lastValue = currentValue
            }
            kotlinx.coroutines.delay(500)
        }
    }
}