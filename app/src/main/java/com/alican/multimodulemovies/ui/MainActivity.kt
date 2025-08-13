package com.alican.multimodulemovies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alican.multimodulemovies.components.bottom_bar.BottomBar
import com.alican.multimodulemovies.components.dialog.FirstTimeThemeDialog
import com.alican.multimodulemovies.components.navigation.MainNavigation
import com.alican.multimodulemovies.navigation.AppRouter
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.theme.MultiModuleMoviesTheme
import com.alican.multimodulemovies.utils.ScreenRoute
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavHostController

    @Inject
    lateinit var appRouter: AppRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            navController = rememberNavController()

            LaunchedEffect(navController) {
                appRouter.setNavController(navController)
            }

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val showBottomBar by remember {
                derivedStateOf {
                    navBackStackEntry?.destination?.hasRoute<ScreenRoute.HomeScreenRoute>() == true ||
                            navBackStackEntry?.destination?.hasRoute<ScreenRoute.SearchScreenRoute>() == true ||
                            navBackStackEntry?.destination?.hasRoute<ScreenRoute.FavoritesScreenRoute>() == true ||
                            navBackStackEntry?.destination?.hasRoute<ScreenRoute.ProfileScreenRoute>() == true
                }
            }

            MultiModuleMoviesTheme(
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
                                BottomBar(
                                    navController = navController,
                                    isBottomBarVisible = showBottomBar
                                )
                            }
                        ) { innerPadding ->
                            MainNavigation(
                                navController = navController,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }
                    }
                }

            }
        }
    }
}