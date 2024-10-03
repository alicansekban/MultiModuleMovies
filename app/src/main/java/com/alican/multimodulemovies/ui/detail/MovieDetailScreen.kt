package com.alican.multimodulemovies.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alican.domain.models.BaseUIModel
import com.alican.multimodulemovies.components.pager.CustomPager
import com.alican.multimodulemovies.ui.detail.components.MovieDetailInformation

@Composable
fun MovieDetailScreen(viewmodel: MovieDetailViewModel = hiltViewModel()) {

    val movieDetail by viewmodel.movieDetail.collectAsStateWithLifecycle()
    val movieImages by viewmodel.movieImages.collectAsStateWithLifecycle()
    val movieCredits by viewmodel.movieCredits.collectAsStateWithLifecycle()
    val movieReviews by viewmodel.movieReviews.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        when (movieImages) {
            BaseUIModel.Empty -> {}
            is BaseUIModel.Error -> {}
            BaseUIModel.Loading -> {}
            is BaseUIModel.Success -> {
                val images = (movieImages as BaseUIModel.Success).data
                CustomPager(images = images, onClick = {})
            }
        }
        when (movieDetail) {
            BaseUIModel.Empty -> {}
            is BaseUIModel.Error -> {}
            BaseUIModel.Loading -> {}
            is BaseUIModel.Success -> {
                val movie = (movieDetail as BaseUIModel.Success).data
                MovieDetailInformation(movie = movie)
            }
        }
        when (movieCredits) {
            BaseUIModel.Empty -> {}
            is BaseUIModel.Error -> {}
            BaseUIModel.Loading -> {}
            is BaseUIModel.Success -> {
                val credits = (movieCredits as BaseUIModel.Success).data
            }
        }
        when (movieReviews) {
            BaseUIModel.Empty -> {}
            is BaseUIModel.Error -> {}
            BaseUIModel.Loading -> {}
            is BaseUIModel.Success -> {
                val reviews = (movieReviews as BaseUIModel.Success).data
            }
        }
    }
}