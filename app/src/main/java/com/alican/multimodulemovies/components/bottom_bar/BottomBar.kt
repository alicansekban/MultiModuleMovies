package com.alican.multimodulemovies.components.bottom_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.utils.ScreenRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun BottomBar(
    navController: NavController,
    isBottomBarVisible: Boolean
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val items = listOf(
        BottomBarRoute(
            name = "Home",
            route = ScreenRoute.HomeHost,
            icon = Icons.Filled.Home
        ),
        BottomBarRoute(
            name = "Search",
            route = ScreenRoute.SearchHost,
            icon = Icons.Filled.Search
        ),
        BottomBarRoute(
            name = "Favorites",
            route = ScreenRoute.FavoritesHost,
            icon = Icons.Filled.Favorite
        ),
        BottomBarRoute(
            name = "Profile",
            route = ScreenRoute.ProfileHost,
            icon = Icons.Filled.Man,
        )
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(visible = isBottomBarVisible) {
            NavigationBar(
                containerColor = AppTheme.colorScheme.cardBackground,
                contentColor = AppTheme.colorScheme.primaryText,
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = {
                            Text(
                                text = item.name,
                                fontSize = 10.sp,
                            )
                        },
                        icon = {
                            Icon(item.icon, contentDescription = item.name)
                        },
                        interactionSource = NoRippleInteractionSource,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AppTheme.colorScheme.accent,
                            selectedTextColor = AppTheme.colorScheme.accent,
                            unselectedIconColor = AppTheme.colorScheme.secondaryText,
                            unselectedTextColor = AppTheme.colorScheme.secondaryText,
                            indicatorColor = AppTheme.colorScheme.accent.copy(alpha = 0.1f),
                            disabledIconColor = AppTheme.colorScheme.statusOffline,
                            disabledTextColor = AppTheme.colorScheme.statusOffline
                        )
                    )
                }
            }
        }
    }
}

private object NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true
}