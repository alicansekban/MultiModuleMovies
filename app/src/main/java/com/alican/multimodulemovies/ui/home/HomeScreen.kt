package com.alican.multimodulemovies.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.models.BaseUIModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val upComingMovies by viewModel.upComingMovies.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {

        when (upComingMovies) {
            BaseUIModel.Empty -> {}
            is BaseUIModel.Error -> {}
            BaseUIModel.Loading -> {}
            is BaseUIModel.Success -> {
                val movies = (upComingMovies as BaseUIModel.Success).data
                LazyRow(
                    Modifier.fillMaxWidth()
                ) {
                    items(movies) {movie ->
                        movie.title?.let {title ->
                            Text(text =title)
                        }
                    }
                }

            }
        }
    }

}