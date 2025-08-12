package com.alican.multimodulemovies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
            if (uiState.showThemeDialog) {
                Dialog(
                    onDismissRequest = {
                        viewModel.setDarkModeFromDialog(false)
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "Your system is in dark mode.\n\nDo you want to change it to dark mode?")
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.setDarkModeFromDialog(true)
                            }
                        ) {
                            Text(text = "Yes")
                        }
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.setDarkModeFromDialog(false)
                            }
                        ) {
                            Text(text = "No")
                        }
                    }
                }
            }
            MultiModuleMoviesTheme(
                isDarkMode = uiState.isDarkMode,
            ) {
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