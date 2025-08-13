
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

    // Theme related
    val FIRST_TIME_USER = booleanPreferencesKey("first_time_user")

    // Notification related
    val HAS_SHOWN_NOTIFICATION_PERMISSION_DIALOG =
        booleanPreferencesKey("has_shown_notification_permission_dialog")
    val NOTIFICATION_PERMISSION_GRANTED = booleanPreferencesKey("notification_permission_granted")
    val PUSH_NOTIFICATIONS_ENABLED = booleanPreferencesKey("push_notifications_enabled")
}