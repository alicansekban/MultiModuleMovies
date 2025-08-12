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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.multimodulemovies.components.imageView.CustomImageViewWithLoading
import com.alican.multimodulemovies.theme.AppTheme
import com.alican.multimodulemovies.utils.heightPercent

@Composable
fun MoviesListScreen(
    modifier: Modifier = Modifier,
    viewModel: MoviesListViewModel = hiltViewModel()
) {
    val uiState by viewModel.movies.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()
    val shouldFetchNextPage by remember {
        derivedStateOf {
            val lastVisibleIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.uiModel.movies.size - 10 &&
                    uiState.uiModel.canLoadMore
        }
    }

    LaunchedEffect(shouldFetchNextPage) {
        if (shouldFetchNextPage) {
            viewModel.updateEvents(event = MovieListUIEvents.GetNextPage)
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

        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            state = gridState,
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.uiModel.movies) { movie ->
                MovieGridItem(
                    imageUrl = movie.imageUrl,
                    title = movie.title,
                    rating = movie.overview,
                    modifier = Modifier.heightPercent(0.45f, configuration)
                )
            }

            // Loading indicator at bottom
            if (uiState.uiModel.canLoadMore) {
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
}

@Composable
private fun MovieListHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardSecondaryBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Movies Collection",
                style = AppTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.primaryText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Browse through our extensive movie library",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.secondaryText,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MovieGridItem(
    imageUrl: String?,
    title: String?,
    rating: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            // Movie Image
            imageUrl?.let {
                CustomImageViewWithLoading(
                    imageUrl = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            // Movie Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                title?.let {
                    Text(
                        text = it,
                        style = AppTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colorScheme.primaryText,
                        maxLines = 2
                    )
                }

                rating?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = AppTheme.colorScheme.accent.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "⭐ $it",
                            style = AppTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.colorScheme.accent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}