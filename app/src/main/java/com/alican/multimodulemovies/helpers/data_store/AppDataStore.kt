package com.alican.multimodulemovies.helpers.data_store

import javax.inject.Inject

class AppDataStore @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    suspend fun setIsLoggedIn(isLoggedIn: Boolean) {
        dataStoreManager.storeValue(
            DataStoreKeys.isLoggedIn,
            isLoggedIn
        )
    }

    suspend fun isLoggedIn(): Boolean {
        return dataStoreManager.readValue(
            DataStoreKeys.isLoggedIn
        ) == true
    }

    suspend fun isDarkMode(): Boolean? {
        return dataStoreManager.readValue(
            DataStoreKeys.isDarkMode
        )
    }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        dataStoreManager.storeValue(
            DataStoreKeys.isDarkMode,
            isDarkMode
        )
    }

    suspend fun getFirebaseToken(): String? {
        return dataStoreManager.readValue(
            DataStoreKeys.FCM_TOKEN_KEY
        )
    }

    suspend fun setFirebaseToken(token: String) {
        dataStoreManager.storeValue(
            DataStoreKeys.FCM_TOKEN_KEY,
            token
        )
    }

    // Theme related
    suspend fun isFirstTimeUser(): Boolean {
        return dataStoreManager.readValue(
            DataStoreKeys.FIRST_TIME_USER
        ) ?: true
    }

    suspend fun setFirstTimeUser(isFirstTime: Boolean) {
        dataStoreManager.storeValue(
            DataStoreKeys.FIRST_TIME_USER,
            isFirstTime
        )
    }

    // Notification related
    suspend fun hasShownNotificationPermissionDialog(): Boolean {
        return dataStoreManager.readValue(
            DataStoreKeys.HAS_SHOWN_NOTIFICATION_PERMISSION_DIALOG
        ) ?: false
    }

    suspend fun setHasShownNotificationPermissionDialog(hasShown: Boolean) {
        dataStoreManager.storeValue(
            DataStoreKeys.HAS_SHOWN_NOTIFICATION_PERMISSION_DIALOG,
            hasShown
        )
    }

    suspend fun isNotificationPermissionGranted(): Boolean {
        return dataStoreManager.readValue(
            DataStoreKeys.NOTIFICATION_PERMISSION_GRANTED
        ) ?: false
    }

    suspend fun setNotificationPermissionGranted(isGranted: Boolean) {
        dataStoreManager.storeValue(
            DataStoreKeys.NOTIFICATION_PERMISSION_GRANTED,
            isGranted
        )
    }

    suspend fun isPushNotificationsEnabled(): Boolean {
        return dataStoreManager.readValue(
            DataStoreKeys.PUSH_NOTIFICATIONS_ENABLED
        ) ?: true
    }

    suspend fun setPushNotificationsEnabled(enabled: Boolean) {
        dataStoreManager.storeValue(
            DataStoreKeys.PUSH_NOTIFICATIONS_ENABLED,
            enabled
        )
    }
}