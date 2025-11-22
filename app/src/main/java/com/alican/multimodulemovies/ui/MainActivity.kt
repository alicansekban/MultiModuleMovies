package com.alican.multimodulemovies.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.multimodulemovies.components.dialog.FirstTimeThemeDialog
import com.alican.multimodulemovies.helpers.navigation3.AppBottomBar
import com.alican.multimodulemovies.helpers.navigation3.AppNavDisplay
import com.alican.multimodulemovies.helpers.navigation3.AppRouter
import com.alican.multimodulemovies.helpers.navigation3.BottomNavRoutes
import com.alican.multimodulemovies.helpers.navigation3.NavigationStateProvider
import com.alican.multimodulemovies.helpers.navigation3.Navigator
import com.alican.multimodulemovies.helpers.navigation3.appEntryProvider
import com.alican.multimodulemovies.helpers.navigation3.rememberNavigationState
import com.alican.multimodulemovies.helpers.navigation3.toEntries
import com.alican.multimodulemovies.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var appRouter: AppRouter

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var navigationStateProvider: NavigationStateProvider


    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        viewModel.updateNotificationPermissionStatus(isGranted)
        if (isGranted) {
            // Permission granted - can now receive notifications
            viewModel.onNotificationPermissionGranted()
        } else {
            // Permission denied - show explanation or alternative
            viewModel.onNotificationPermissionDenied()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check and request notification permission on app start
        checkAndRequestNotificationPermission()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()


            val bottomBarItems = listOf(
                BottomNavRoutes.Home,
                BottomNavRoutes.Search,
                BottomNavRoutes.Favorites,
                BottomNavRoutes.Profile
            )
            val navigationState = rememberNavigationState(
                startRoute = BottomNavRoutes.Home,
                topLevelRoutes = bottomBarItems.toSet()
            )
            // Initialize the navigation state provider once
            LaunchedEffect(Unit) {
                navigationStateProvider.initialize(navigationState)
            }


            val entryProvider = appEntryProvider(navigator)

            AppTheme(
                isDarkMode = uiState.isDarkMode,
            ) {

                SideEffect {
                    val window = window
                    val insetsController =
                        WindowCompat.getInsetsController(window, window.decorView)

                    // Only set icon colors - let the padding handle the visual appearance
                    insetsController.isAppearanceLightStatusBars = !uiState.isDarkMode
                    insetsController.isAppearanceLightNavigationBars = !uiState.isDarkMode
                }

                if (uiState.showThemeDialog) {
                    FirstTimeThemeDialog(
                        onDismiss = {
                            viewModel.setDarkModeFromDialog(false)
                        },
                        onYesClicked = {
                            viewModel.setDarkModeFromDialog(true)
                        },
                        onNoClicked = {
                            viewModel.setDarkModeFromDialog(false)
                        }
                    )
                }

                // Show notification permission dialog
                if (uiState.showNotificationPermissionDialog) {
                    NotificationPermissionDialog(
                        onRequestPermission = {
                            requestNotificationPermission()
                        },
                        onDismiss = {
                            viewModel.dismissNotificationPermissionDialog()
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colorScheme.primaryBackground)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Status bar area with custom background

                        // Main content area
                        Scaffold(
                            modifier = Modifier.weight(1f),
                            containerColor = AppTheme.colorScheme.primaryBackground,
                            bottomBar = {
                                val isBottomBarVisible =
                                    navigationState.backStacks[navigationState.topLevelRoute]?.size == 1
                                if (isBottomBarVisible) {
                                    AppBottomBar(
                                        navigationState = navigationState,
                                        bottomBarItems = bottomBarItems,
                                        navigator = navigator
                                    )
                                }
                            }
                        ) { innerPadding ->
                            AppNavDisplay(
                                entries = navigationState.toEntries(entryProvider = entryProvider),
                                modifier = Modifier.padding(innerPadding),
                                navigator = navigator,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                checkNotificationPermission() -> {
                    // Permission already granted
                    viewModel.updateNotificationPermissionStatus(true)
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Show rationale dialog
                    viewModel.showNotificationPermissionDialog()
                }

                else -> {
                    // Request permission directly
                    requestNotificationPermission()
                }
            }
        } else {
            // Pre Android 13, no explicit permission needed
            viewModel.updateNotificationPermissionStatus(true)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

}


@Composable
fun NotificationPermissionDialog(
    onRequestPermission: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Enable Notifications",
                style = AppTheme.typography.titleLarge,
                color = AppTheme.colorScheme.primaryText
            )
        },
        text = {
            Text(
                text = "Stay updated with the latest movie releases, trending shows, and personalized recommendations. Enable notifications to never miss out on great content!",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.secondaryText
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onRequestPermission()
                    onDismiss()
                }
            ) {
                Text(
                    text = "Enable",
                    style = AppTheme.typography.labelLarge,
                    color = AppTheme.colorScheme.primaryButton
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Not Now",
                    style = AppTheme.typography.labelLarge,
                    color = AppTheme.colorScheme.secondaryText
                )
            }
        },
        containerColor = AppTheme.colorScheme.cardBackground,
        tonalElevation = 8.dp
    )
}