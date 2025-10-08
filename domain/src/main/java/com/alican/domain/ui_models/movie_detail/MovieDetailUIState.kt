package com.alican.domain.ui_models.movie_detail

import com.alican.domain.ui_models.movie.MovieCreditsUIModel
import com.alican.domain.ui_models.movie.MovieDetailUIModel
import com.alican.domain.ui_models.movie.MovieReviewsUIModel

data class MovieDetailUIState(
    val movieDetail: MovieDetailUIModel? = null,
    val movieImages: List<String> = emptyList(),
    val movieCredits: List<MovieCreditsUIModel> = emptyList(),
    val movieReviews: List<MovieReviewsUIModel> = emptyList(),
    val isLoading: Boolean = false,
)
