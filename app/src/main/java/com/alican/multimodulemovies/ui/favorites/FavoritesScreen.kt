package com.alican.multimodulemovies.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.models.MovieUIModel
import com.alican.multimodulemovies.components.card.EmptyStateCard
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.ui.list.components.MovieGridItem
import com.alican.multimodulemovies.utils.CollectFlowAsEvent
import com.alican.multimodulemovies.utils.heightPercent
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }


    CollectFlowAsEvent(viewModel.uiEffect) { effect ->
        when (effect) {
            is FavoritesUIEffects.ShowError -> {
                scope.launch {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }

            is FavoritesUIEffects.ShowToast -> {
                scope.launch {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }




    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
    ) {
        FavoritesScreenContent(
            modifier = modifier,
            uiState = uiState,
            onEvent = viewModel::handleEvent
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}

@Composable
fun FavoritesScreenContent(
    modifier: Modifier = Modifier,
    uiState: FavoritesUIState,
    onEvent: (FavoritesUIEvents) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val gridState = rememberLazyGridState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
    ) {
        // Header
        FavoritesHeader(favoritesCount = uiState.favoritesCount)

        when {
            // Loading state
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = AppTheme.colorScheme.primaryButton
                    )
                }
            }

            // Empty state
            uiState.isEmpty -> {
                EmptyStateCard(
                    message = "No favorite movies yet!\nStart adding movies to your favorites to see them here.",

                    )
            }

            // Content with favorites
            else -> {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = uiState.favoriteMovies,
                        key = { it.id ?: UUID.randomUUID().toString() }
                    ) { movie ->
                        MovieGridItem(
                            imageUrl = movie.imageUrl,
                            title = movie.title,
                            isFavorite = true, // Always true in favorites screen
                            modifier = Modifier.heightPercent(0.30f, configuration),
                            onClick = {
                                movie.id?.let { movieId ->
                                    onEvent(FavoritesUIEvents.OpenMovieDetail(movieId))
                                }
                            },
                            onFavoriteClick = {
                                movie.id?.let { movieId ->
                                    onEvent(FavoritesUIEvents.RemoveFromFavorites(movieId))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesHeader(
    favoritesCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colorScheme.primaryBackground)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "My Favorites",
                style = AppTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.primaryText,
                textAlign = TextAlign.Center
            )

            if (favoritesCount > 0) {
                Text(
                    text = "$favoritesCount movie${if (favoritesCount != 1) "s" else ""} in your collection",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.secondaryText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// Preview Composables
@Preview(name = "Favorites Screen - Loading")
@Composable
private fun FavoritesLoadingPreview() {
    AppTheme(isDarkMode = false) {
        FavoritesScreenContent(
            uiState = FavoritesUIState(
                isLoading = true,
                favoriteMovies = emptyList(),
                favoritesCount = 0
            )
        )
    }
}

@Preview(name = "Favorites Screen - Empty")
@Composable
private fun FavoritesEmptyPreview() {
    AppTheme(isDarkMode = false) {
        FavoritesScreenContent(
            uiState = FavoritesUIState(
                isLoading = false,
                favoriteMovies = emptyList(),
                favoritesCount = 0
            )
        )
    }
}

@Preview(name = "Favorites Screen - With Data Light")
@Composable
private fun FavoritesWithDataPreview() {
    AppTheme(isDarkMode = false) {
        val sampleMovies = listOf(
            MovieUIModel(
                id = 1,
                title = "The Amazing Spider-Man",
                overview = "A great superhero movie",
                isFavorite = true
            ),
            MovieUIModel(
                id = 2,
                title = "Inception",
                overview = "A mind-bending thriller",
                isFavorite = true
            ),
            MovieUIModel(
                id = 3,
                title = "The Dark Knight",
                overview = "Batman's greatest challenge",
                isFavorite = true
            ),
            MovieUIModel(
                id = 4,
                title = "Avengers: Endgame",
                overview = "The epic conclusion",
                isFavorite = true
            )
        )

        FavoritesScreenContent(
            uiState = FavoritesUIState(
                isLoading = false,
                favoriteMovies = sampleMovies,
                favoritesCount = sampleMovies.size
            )
        )
    }
}

@Preview(name = "Favorites Screen - With Data Dark")
@Composable
private fun FavoritesWithDataDarkPreview() {
    AppTheme(isDarkMode = true) {
        val sampleMovies = listOf(
            MovieUIModel(
                id = 1,
                title = "The Amazing Spider-Man",
                overview = "A great superhero movie",
                isFavorite = true
            ),
            MovieUIModel(
                id = 2,
                title = "Inception",
                overview = "A mind-bending thriller",
                isFavorite = true
            )
        )

        FavoritesScreenContent(
            uiState = FavoritesUIState(
                isLoading = false,
                favoriteMovies = sampleMovies,
                favoritesCount = sampleMovies.size
            )
        )
    }
}

@Preview(name = "Favorites Header")
@Composable
private fun FavoritesHeaderPreview() {
    AppTheme(isDarkMode = false) {
        FavoritesHeader(favoritesCount = 12)
    }
}