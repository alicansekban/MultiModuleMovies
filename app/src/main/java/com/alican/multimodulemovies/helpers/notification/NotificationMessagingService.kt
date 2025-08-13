package com.alican.multimodulemovies.helpers.notification


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.alican.multimodulemovies.R
import com.alican.multimodulemovies.helpers.data_store.AppDataStore
import com.alican.multimodulemovies.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var appDataStore: AppDataStore
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        private const val TAG = "FCMService"
        const val CHANNEL_ID = "movie_notifications"
        const val CHANNEL_NAME = "Movie Updates"
        const val NOTIFICATION_ID_BASE = 1000
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.d(TAG, "NotificationMessagingService created")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token: $token")

        serviceScope.launch {
            try {
                // Store token locally
                appDataStore.setFirebaseToken(token)

                Log.d(TAG, "Token saved successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save token", e)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "Message received from: ${remoteMessage.from}")

        serviceScope.launch {
            try {
                // Save notification to local database
                val notification = createNotificationModel(remoteMessage)
                //  notificationRepository.saveNotification(notification)

                // Show notification to user
                showNotification(remoteMessage)

                Log.d(TAG, "Notification processed successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to process notification", e)
            }
        }
    }

    private fun createNotificationModel(remoteMessage: RemoteMessage): NotificationModel {
        return NotificationModel(
            id = System.currentTimeMillis().toString(),
            title = remoteMessage.notification?.title ?: "Movie Update",
            body = remoteMessage.notification?.body ?: "",
            imageUrl = remoteMessage.notification?.imageUrl?.toString(),
            data = remoteMessage.data,
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: "Movie Update"
        val body = remoteMessage.notification?.body ?: ""
        val imageUrl = remoteMessage.notification?.imageUrl?.toString()

        val intent = createNotificationIntent(remoteMessage.data)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // You'll need to add this icon
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))

        // Load and set big picture if image URL is provided
        showNotificationWithId(notificationBuilder.build())
    }

    private fun createNotificationIntent(data: Map<String, String>): Intent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Handle different notification types
        when (data["type"]) {
            "movie_detail" -> {
                data["movie_id"]?.let { movieId ->
                    intent.putExtra("navigate_to", "movie_detail")
                    intent.putExtra("movie_id", movieId.toIntOrNull() ?: 0)
                }
            }

            "movie_list" -> {
                data["movie_type"]?.let { movieType ->
                    intent.putExtra("navigate_to", "movie_list")
                    intent.putExtra("movie_type", movieType)
                }
            }

            else -> {
                intent.putExtra("navigate_to", "home")
            }
        }

        return intent
    }

    private fun showNotificationWithId(notification: android.app.Notification) {
        val notificationId = NOTIFICATION_ID_BASE + System.currentTimeMillis().toInt()

        try {
            NotificationManagerCompat.from(this).notify(notificationId, notification)
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied for showing notification", e)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for movie updates and recommendations"
            enableLights(true)
            enableVibration(true)
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "NotificationMessagingService destroyed")
    }
}

data class NotificationModel(
    val id: String,
    val title: String,
    val body: String,
    val imageUrl: String? = null,
    val data: Map<String, String> = emptyMap(),
    val timestamp: Long,
    val isRead: Boolean = false
)