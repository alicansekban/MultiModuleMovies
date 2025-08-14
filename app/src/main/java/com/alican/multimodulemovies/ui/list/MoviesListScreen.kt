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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.multimodulemovies.components.card.EmptyStateCard
import com.alican.multimodulemovies.components.card.LoadingStateCard
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.ui.list.components.ErrorStateCard
import com.alican.multimodulemovies.ui.list.components.MovieGridItem
import com.alican.multimodulemovies.ui.list.components.MovieListHeader
import com.alican.multimodulemovies.utils.heightPercent
import java.util.UUID

@Composable
fun MoviesListScreen(
    modifier: Modifier = Modifier,
    viewModel: MoviesListViewModel = hiltViewModel()
) {
    val paginationState by viewModel.movies.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()

    // Keep your derivedState logic but use new pagination state
    val shouldFetchNextPage by remember {
        derivedStateOf {
            val lastVisibleIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            lastVisibleIndex != null &&
                    lastVisibleIndex >= paginationState.items.size - 10 &&
                    paginationState.hasNextPage &&
                    !paginationState.isLoadingMore
        }
    }

    LaunchedEffect(shouldFetchNextPage) {
        if (shouldFetchNextPage) {
            viewModel.loadNextPage()
        }
    }

    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
    ) {
        // Header Section
        MovieListHeader()

        // Show initial loading state
        if (paginationState.isLoading && paginationState.items.isEmpty()) {
            LoadingStateCard()
            return@Column
        }

        // Show error state for first page
        if (paginationState.hasError && paginationState.items.isEmpty()) {
            ErrorStateCard(
                message = paginationState.errorMessage ?: "Unknown error occurred",
                onRetry = { viewModel.retry() }
            )
            return@Column
        }

        // Show empty state
        if (paginationState.isEmpty) {
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
            items(paginationState.items, key = { it.id ?: UUID.randomUUID().toString() }) { movie ->
                MovieGridItem(
                    imageUrl = movie.imageUrl,
                    title = movie.title,
                    modifier = Modifier.heightPercent(0.45f, configuration)
                )
            }

            // Bottom loading indicator for pagination
            if (paginationState.isLoadingMore) {
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
            if (paginationState.hasError && paginationState.items.isNotEmpty()) {
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
                                onClick = { viewModel.retry() }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }

            // End of list indicator
            if (paginationState.items.isNotEmpty() && !paginationState.hasNextPage && !paginationState.isLoadingMore) {
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
                                text = "You've reached the end! 🎬\n${paginationState.totalResults} movies total",
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