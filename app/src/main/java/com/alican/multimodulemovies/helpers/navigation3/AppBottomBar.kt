package com.alican.multimodulemovies.helpers.navigation3

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alican.navigation3.navigation.NavigationState

@Composable
fun AppBottomBar(
    navigationState: NavigationState,
    navigator: Navigator,
    bottomBarItems: List<BottomNavRoutes>
) {

    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        bottomBarItems.forEach { item ->
            val isSelected = item == navigationState.topLevelRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = { navigator.navigate(item) },
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