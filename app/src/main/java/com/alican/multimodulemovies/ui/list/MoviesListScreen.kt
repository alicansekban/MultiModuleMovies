package com.alican.multimodulemovies.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.models.BaseUIModel
import com.alican.domain.models.MovieListUIModel
import com.alican.domain.models.MovieUIModel
import com.alican.multimodulemovies.components.imageView.CustomImageView
import com.alican.multimodulemovies.components.imageView.CustomImageViewWithLoading
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
    Column(Modifier.fillMaxSize()) {


        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            state = gridState
        ) {
            items(uiState.uiModel.movies) {
                it.imageUrl?.let { it1 ->
                    CustomImageViewWithLoading(
                        imageUrl = it1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightPercent(0.40f, configuration)
                    )
                }
            }
        }
    }
}