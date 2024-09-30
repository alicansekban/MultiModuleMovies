package com.alican.multimodulemovies.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.models.BaseUIModel

@Composable
fun MovieDetailScreen(viewmodel: MovieDetailViewModel = hiltViewModel()) {

    val movieDetail by viewmodel.movieDetail.collectAsStateWithLifecycle()

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {

        Text(text = "Movie Detail Screen")

        when (movieDetail) {
            BaseUIModel.Empty -> {}
            is BaseUIModel.Error -> {}
            BaseUIModel.Loading -> {}
            is BaseUIModel.Success -> {
                val movie = (movieDetail as BaseUIModel.Success).data
                movie.title?.let { Text(text = it) }
            }
        }
    }
}