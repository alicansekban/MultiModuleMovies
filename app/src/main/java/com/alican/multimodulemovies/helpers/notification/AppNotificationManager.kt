package com.alican.multimodulemovies.helpers.notification


import android.content.Context
import android.util.Log
import com.alican.multimodulemovies.helpers.data_store.AppDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class NotificationSettings(
    val pushNotificationsEnabled: Boolean = true,
    val movieUpdatesEnabled: Boolean = true,
    val recommendationsEnabled: Boolean = true,
    val promotionsEnabled: Boolean = false,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true
)


@Singleton
class AppNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appDataStore: AppDataStore
) {
    companion object Companion {
        private const val TAG = "NotificationManager"
        private const val MAX_NOTIFICATIONS = 50
    }

    // In-memory notification storage (later replace with Room)
    private val _notifications = MutableStateFlow<List<NotificationModel>>(emptyList())
    val notifications: Flow<List<NotificationModel>> = _notifications.asStateFlow()

    private val _notificationSettings = MutableStateFlow(NotificationSettings())
    val notificationSettings: Flow<NotificationSettings> = _notificationSettings.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: Flow<Int> = _unreadCount.asStateFlow()

    init {
        loadNotificationSettings()
    }

    private fun loadNotificationSettings() {
        // Load settings from DataStore or use defaults
        _notificationSettings.value = NotificationSettings(
            pushNotificationsEnabled = true,
            movieUpdatesEnabled = true,
            recommendationsEnabled = true,
            promotionsEnabled = false
        )
    }

    suspend fun saveFirebaseToken(token: String) {
        try {
            appDataStore.setFirebaseToken(token)
            Log.d(TAG, "FCM token saved successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save FCM token", e)
        }
    }

    suspend fun getFirebaseToken(): String? {
        return try {
            appDataStore.getFirebaseToken()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get FCM token", e)
            null
        }
    }

    fun saveNotification(notification: NotificationModel) {
        try {
            val currentNotifications = _notifications.value.toMutableList()

            // Add new notification at the beginning
            currentNotifications.add(0, notification)

            // Keep only the latest MAX_NOTIFICATIONS
            if (currentNotifications.size > MAX_NOTIFICATIONS) {
                currentNotifications.removeAt(currentNotifications.size - 1)
            }

            _notifications.value = currentNotifications

            Log.d(TAG, "Notification saved: ${notification.title}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save notification", e)
        }
    }

    fun updateNotificationSettings(settings: NotificationSettings) {
        try {
            _notificationSettings.value = settings
            // TODO: Save to DataStore when you add the keys
            Log.d(TAG, "Notification settings updated")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update notification settings", e)
        }
    }

    fun isNotificationPermissionGranted(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            true // Pre Android 13, permission not required
        }
    }
}