package com.alican.multimodulemovies.helpers.navigation3

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay


@Composable
fun AppNavDisplay(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    entries: List<NavEntry<NavKey>>,
) {

    NavDisplay(
        modifier = modifier,
        onBack = { navigator.goBack() },
        entries = entries

    )
}