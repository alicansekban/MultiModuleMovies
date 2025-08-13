package com.alican.multimodulemovies.helpers.data_store

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    //pref-name
    const val PREFERENCES_NAME = "movies-preferences"

    //pref-keys
    val isLoggedIn = booleanPreferencesKey("isLoggedIn")
    val isDarkMode = booleanPreferencesKey("isDarkMode")
    val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")

}