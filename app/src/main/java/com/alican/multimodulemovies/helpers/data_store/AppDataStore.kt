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
}