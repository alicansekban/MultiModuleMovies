package com.alican.multimodulemovies.helpers.navigation3.display

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.alican.multimodulemovies.helpers.navigation3.router.AppRouter


@Composable
fun AppNavDisplay(
    modifier: Modifier = Modifier,
    appRouter: AppRouter,
    entries: List<NavEntry<NavKey>>,
) {

    NavDisplay(
        modifier = modifier,
        onBack = { appRouter.navigateBack() },
        entries = entries

    )
}