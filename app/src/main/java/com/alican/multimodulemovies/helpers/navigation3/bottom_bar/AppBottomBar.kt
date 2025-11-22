package com.alican.multimodulemovies.helpers.navigation3.bottom_bar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alican.multimodulemovies.helpers.navigation3.entry.BottomNavRoutes
import com.alican.multimodulemovies.helpers.navigation3.router.AppRouter
import com.alican.multimodulemovies.helpers.navigation3.state.NavigationState

@Composable
fun AppBottomBar(
    navigationState: NavigationState,
    bottomBarItems: List<BottomNavRoutes>,
    appRouter: AppRouter
) {

    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        bottomBarItems.forEach { item ->
            val isSelected = item == navigationState.topLevelRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = { appRouter.navigateTo(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) }
            )
        }
    }
}