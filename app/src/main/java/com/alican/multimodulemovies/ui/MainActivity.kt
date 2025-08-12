package com.alican.multimodulemovies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alican.multimodulemovies.components.FirstTimeThemeDialog
import com.alican.multimodulemovies.components.bottom_bar.BottomBar
import com.alican.multimodulemovies.components.navigation.MainNavigation
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.theme.MultiModuleMoviesTheme
import com.alican.multimodulemovies.utils.ScreenRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            navController = rememberNavController()
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
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colorScheme.primaryBackground),
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            isBottomBarVisible = showBottomBar
                        )
                    }
                ) { innerPadding ->
                    MainNavigation(
                        navController = navController, modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}