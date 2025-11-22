package com.alican.multimodulemovies.helpers.navigation3.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.alican.multimodulemovies.helpers.navigation3.router.AppRouter
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.ui.detail.MovieDetailScreen
import com.alican.multimodulemovies.ui.detail.MovieDetailViewModel
import com.alican.multimodulemovies.ui.list.MoviesListScreen
import com.alican.multimodulemovies.ui.list.MoviesListViewModel
import com.alican.multimodulemovies.ui.login.LoginScreen
import com.alican.multimodulemovies.ui.register.RegisterScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun appEntryProvider(
    appRouter: AppRouter
): (NavKey) -> NavEntry<NavKey> {
    val entryProvider = entryProvider {
        HomeEntry(appRouter)
        SearchEntry(appRouter)
        FavoritesEntry(appRouter)
        ProfileEntry(appRouter)

        entry<EntryRoutes.LoginEntryRoutes> {
            LoginScreen()
        }
        entry<EntryRoutes.RegisterEntryRoutes> {
            RegisterScreen()
        }
        entry<EntryRoutes.MoviesListRoute>(
            metadata = ListDetailSceneStrategy.listPane(
                detailPlaceholder = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AppTheme.colorScheme.primaryBackground)
                    )
                }
            )
        ) { key ->
            val viewModel = hiltViewModel<MoviesListViewModel, MoviesListViewModel.Factory>(
                creationCallback = { factory ->
                    factory.create(key)

                }
            )
            MoviesListScreen(
                viewModel = viewModel
            )
        }
        entry<EntryRoutes.MovieDetailRoute>(
            metadata = ListDetailSceneStrategy.detailPane()
        ) { key ->
            val viewModel = hiltViewModel<MovieDetailViewModel, MovieDetailViewModel.Factory>(
                creationCallback = { factory ->
                    factory.create(key)
                }
            )
            MovieDetailScreen(viewModel = viewModel)
        }
    }
    return entryProvider
}