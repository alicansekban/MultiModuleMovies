package com.alican.multimodulemovies.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.ui_models.movie.MovieUIModel
import com.alican.domain.ui_models.pagination.PaginationUIModel
import com.alican.multimodulemovies.components.card.EmptyStateCard
import com.alican.multimodulemovies.components.card.LoadingStateCard
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.ui.list.components.ErrorStateCard
import com.alican.multimodulemovies.ui.list.components.MovieGridItem
import com.alican.multimodulemovies.ui.list.components.MovieListHeader
import com.alican.multimodulemovies.utils.heightPercent

@Composable
fun MoviesListScreen(
    modifier: Modifier = Modifier,
    viewModel: MoviesListViewModel
) {
    val paginationState by viewModel.movies.collectAsStateWithLifecycle()

    val gridState = rememberLazyGridState()

    // Keep your derivedState logic
    val shouldFetchNextPage by remember {
        derivedStateOf {
            val lastVisibleIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            lastVisibleIndex != null &&
                    lastVisibleIndex >= paginationState.items.size - 10 &&
                    paginationState.canLoadMore &&
                    !paginationState.isLoadingMore &&
                    !paginationState.hasError
        }
    }


    LaunchedEffect(shouldFetchNextPage) {
        if (shouldFetchNextPage) {
            viewModel.onScreenEvent(MovieListUIEvents.LoadNextPage)
        }
    }


    MoviesListScreenContent(
        modifier = modifier,
        gridState = gridState,
        uiState = paginationState,
        onEvent = viewModel::onScreenEvent
    )
}

@Composable
fun MoviesListScreenContent(
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    uiState: PaginationUIModel<MovieUIModel>,
    onEvent: (MovieListUIEvents) -> Unit = {}
) {


    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
    ) {
        // Header Section
        MovieListHeader()

        // Show initial loading state
        if (uiState.isLoading && uiState.items.isEmpty()) {
            LoadingStateCard()
            return@Column
        }

        // Show error state for first page
        if (uiState.hasError && uiState.items.isEmpty()) {
            ErrorStateCard(
                message = uiState.errorMessage ?: "Unknown error occurred",
                onRetry = { onEvent(MovieListUIEvents.Retry) }
            )
            return@Column
        }

        // Show empty state
        if (uiState.isEmpty) {
            EmptyStateCard("No movies found")
            return@Column
        }

        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            state = gridState,
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Movie items
            items(uiState.items, key = { it.id }) { movie ->
                MovieGridItem(
                    isFavorite = movie.isFavorite,
                    imageUrl = movie.imageUrl,
                    title = movie.title,
                    modifier = Modifier.heightPercent(0.30f, configuration),
                    onClick = { onEvent(MovieListUIEvents.OpenMovieDetail(movie.id ?: 0)) },
                    onFavoriteClick = { onEvent(MovieListUIEvents.ToggleFavorite(movie)) }
                )
            }

            // Bottom loading indicator for pagination
            if (uiState.isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = AppTheme.colorScheme.primaryButton
                        )
                    }
                }
            }

            // Error indicator for pagination
            if (uiState.hasError && uiState.items.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AppTheme.colorScheme.errorColor.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Failed to load more movies",
                                style = AppTheme.typography.bodyMedium,
                                color = AppTheme.colorScheme.errorColor,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onEvent(MovieListUIEvents.Retry) }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }

            // End of list indicator
            if (uiState.items.isNotEmpty() && !uiState.hasNextPage && !uiState.isLoadingMore) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AppTheme.colorScheme.cardSecondaryBackground
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "You've reached the end! 🎬\n${uiState.totalResults} movies total",
                                style = AppTheme.typography.bodyMedium,
                                color = AppTheme.colorScheme.secondaryText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

// Preview Composables
@Preview(name = "Movies List Light - Loading")
@Composable
private fun MoviesListLoadingPreview() {
    AppTheme(isDarkMode = false) {
        MoviesListScreenContent(
            uiState = PaginationUIModel(
                items = emptyList(),
                isLoading = true,
                isLoadingMore = false,
                hasError = false,
                canLoadMore = true,
                currentPage = 1,
                totalResults = 0,
                errorMessage = null
            )
        )
    }
}

@Preview(name = "Movies List Dark - Loading")
@Composable
private fun MoviesListLoadingDarkPreview() {
    AppTheme(isDarkMode = true) {
        MoviesListScreenContent(
            uiState = PaginationUIModel(
                items = emptyList(),
                isLoading = true,
                isLoadingMore = false,
                hasError = false,
                currentPage = 1,
                totalResults = 0,
                errorMessage = null
            )
        )
    }
}

@Preview(name = "Movies List Light - Error")
@Composable
private fun MoviesListErrorPreview() {
    AppTheme(isDarkMode = false) {
        MoviesListScreenContent(
            uiState = PaginationUIModel(
                items = emptyList(),
                isLoading = false,
                isLoadingMore = false,
                hasError = true,
                currentPage = 1,
                totalResults = 0,
                errorMessage = "Failed to load movies. Please check your connection."
            )
        )
    }
}

@Preview(name = "Movies List Dark - Error")
@Composable
private fun MoviesListErrorDarkPreview() {
    AppTheme(isDarkMode = true) {
        MoviesListScreenContent(
            uiState = PaginationUIModel(
                items = emptyList(),
                isLoading = false,
                isLoadingMore = false,
                hasError = true,
                currentPage = 1,
                totalResults = 0,
                errorMessage = "Network error occurred"
            )
        )
    }
}

@Preview(name = "Movies List Light - Empty")
@Composable
private fun MoviesListEmptyPreview() {
    AppTheme(isDarkMode = false) {
        MoviesListScreenContent(
            uiState = PaginationUIModel(
                items = emptyList(),
                isLoading = false,
                isLoadingMore = false,
                hasError = false,
                currentPage = 1,
                totalResults = 0,
                errorMessage = null
            )
        )
    }
}

@Preview(name = "Movies List Dark - Empty")
@Composable
private fun MoviesListEmptyDarkPreview() {
    AppTheme(isDarkMode = true) {
        MoviesListScreenContent(
            uiState = PaginationUIModel(
                items = emptyList(),
                isLoading = false,
                isLoadingMore = false,
                hasError = false,
                currentPage = 1,
                totalResults = 0,
                errorMessage = null
            )
        )
    }
}

@Preview(name = "Movies List Light - With Data")
@Composable
private fun MoviesListWithDataPreview() {
    AppTheme(isDarkMode = false) {
        val sampleMovies = listOf(
            MovieUIModel(
                id = 1,
                title = "The Amazing Spider-Man",
                overview = "A great superhero movie",
            ),
            MovieUIModel(
                id = 2,
                title = "Inception",
                overview = "A mind-bending thriller",
            ),
            MovieUIModel(
                id = 3,
                title = "The Dark Knight",
                overview = "Batman's greatest challenge",
            ),
            MovieUIModel(
                id = 4,
                title = "Avengers: Endgame",
                overview = "The epic conclusion",
            )
        )

        MoviesListScreenContent(
            uiState = PaginationUIModel(
                items = sampleMovies,
                isLoading = false,
                isLoadingMore = false,
                hasError = false,
                currentPage = 1,
                totalResults = 150,
                errorMessage = null
            )
        )
    }
}

@Preview(name = "Movies List Dark - With Data")
@Composable
private fun MoviesListWithDataDarkPreview() {
    AppTheme(isDarkMode = true) {
        val sampleMovies = listOf(
            MovieUIModel(
                id = 1,
                title = "The Amazing Spider-Man",
                overview = "A great superhero movie",
            ),
            MovieUIModel(
                id = 2,
                title = "Inception",
                overview = "A mind-bending thriller",
            )
        )

        MoviesListScreenContent(
            uiState = PaginationUIModel(
                items = sampleMovies,
                isLoading = false,
                isLoadingMore = false,
                hasError = false,
                currentPage = 1,
                totalResults = 75,
                errorMessage = null
            )
        )
    }
}

@Preview(name = "Movies List Light - Loading More")
@Composable
private fun MoviesListLoadingMorePreview() {
    AppTheme(isDarkMode = false) {
        val sampleMovies = listOf(
            MovieUIModel(
                id = 1,
                title = "The Amazing Spider-Man",
                overview = "A great superhero movie",
            ),
            MovieUIModel(
                id = 2,
                title = "Inception",
                overview = "A mind-bending thriller",
            )
        )

        MoviesListScreenContent(
            uiState = PaginationUIModel(
                items = sampleMovies,
                isLoading = false,
                isLoadingMore = true,
                hasError = false,
                currentPage = 1,
                totalResults = 200,
                errorMessage = null
            )
        )
    }
}

@Preview(name = "Movies List Light - End of List")
@Composable
private fun MoviesListEndOfListPreview() {
    AppTheme(isDarkMode = false) {
        val sampleMovies = listOf(
            MovieUIModel(
                id = 1,
                title = "The Amazing Spider-Man",
                overview = "A great superhero movie",
            )
        )

        MoviesListScreenContent(
            uiState = PaginationUIModel(
                items = sampleMovies,
                isLoading = false,
                isLoadingMore = false,
                hasError = false,
                currentPage = 5,
                totalResults = 50,
                errorMessage = null
            )
        )
    }
}