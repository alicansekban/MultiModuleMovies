package com.alican.multimodulemovies.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.models.MovieUIModel
import com.alican.domain.models.pagination.PaginationUIModel
import com.alican.multimodulemovies.components.card.EmptyStateCard
import com.alican.multimodulemovies.components.card.LoadingStateCard
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.ui.list.components.ErrorStateCard
import com.alican.multimodulemovies.ui.list.components.MovieGridItem
import com.alican.multimodulemovies.utils.heightPercent
import java.util.UUID

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()

    // Handle infinite scrolling
    val shouldFetchNextPage by remember {
        derivedStateOf {
            val lastVisibleIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            lastVisibleIndex != null &&
                    lastVisibleIndex >= movies.items.size - 10 &&
                    movies.canLoadMore &&
                    !movies.isLoadingMore &&
                    !movies.hasError &&
                    searchQuery.isNotBlank()
        }
    }

    LaunchedEffect(shouldFetchNextPage) {
        if (shouldFetchNextPage) {
            viewModel.onScreenEvent(SearchUIEvents.OnLoadMore)
        }
    }

    SearchScreenContent(
        modifier = modifier,
        gridState = gridState,
        searchQuery = searchQuery,
        uiState = movies,
        onEvent = viewModel::onScreenEvent
    )
}

@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    searchQuery: String,
    uiState: PaginationUIModel<MovieUIModel>,
    onEvent: (SearchUIEvents) -> Unit = {}
) {
    val configuration = LocalConfiguration.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primaryBackground)
    ) {
        // Search Input
        SearchTextField(
            query = searchQuery,
            onQueryChanged = { onEvent(SearchUIEvents.OnQueryChanged(it)) },
            onClearClicked = { onEvent(SearchUIEvents.OnClearSearch) },
            modifier = Modifier.padding(16.dp)
        )

        // Content based on state
        when {
            searchQuery.isEmpty() -> {
                SearchEmptyState()
            }

            uiState.isLoading && uiState.items.isEmpty() -> {
                LoadingStateCard()
            }

            uiState.hasError && uiState.items.isEmpty() -> {
                ErrorStateCard(
                    message = uiState.errorMessage ?: "Failed to search movies",
                    onRetry = { onEvent(SearchUIEvents.OnRetryClicked) }
                )
            }

            uiState.isEmpty -> {
                EmptyStateCard("No movies found for \"$searchQuery\"")
            }

            else -> {
                SearchResultsList(
                    uiState = uiState,
                    gridState = gridState,
                    configuration = configuration,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Search movies...",
                color = AppTheme.colorScheme.secondaryText
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = AppTheme.colorScheme.secondaryText
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearClicked) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = AppTheme.colorScheme.secondaryText
                    )
                }
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppTheme.colorScheme.primaryButton,
            unfocusedBorderColor = AppTheme.colorScheme.secondaryText.copy(alpha = 0.3f),
            focusedTextColor = AppTheme.colorScheme.primaryText,
            unfocusedTextColor = AppTheme.colorScheme.primaryText
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { keyboardController?.hide() }
        ),
        singleLine = true
    )
}

@Composable
private fun SearchEmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = AppTheme.colorScheme.primaryButton.copy(alpha = 0.6f)
            )
            Text(
                text = "Search for movies",
                style = AppTheme.typography.headlineSmall,
                color = AppTheme.colorScheme.primaryText,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Find your favorite movies, actors, and genres",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.secondaryText,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SearchResultsList(
    uiState: PaginationUIModel<MovieUIModel>,
    gridState: LazyGridState,
    configuration: Configuration,
    onEvent: (SearchUIEvents) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        state = gridState,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Movie items
        items(uiState.items, key = { it.id ?: UUID.randomUUID().toString() }) { movie ->
            MovieGridItem(
                isFavorite = movie.isFavorite,
                imageUrl = movie.imageUrl,
                title = movie.title,
                modifier = Modifier.heightPercent(0.30f, configuration),
                onClick = { onEvent(SearchUIEvents.OnMovieClicked(movie.id ?: 0)) },
                onFavoriteClick = { onEvent(SearchUIEvents.OnFavoriteClicked(movie)) }
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
    }
}