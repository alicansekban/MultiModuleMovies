package com.alican.domain.models.movie_detail

import com.alican.domain.models.MovieCreditsUIModel
import com.alican.domain.models.MovieDetailUIModel
import com.alican.domain.models.MovieReviewsUIModel

data class MovieDetailUIState(
    val movieDetail: MovieDetailUIModel? = null,
    val movieImages: List<String> = emptyList(),
    val movieCredits: List<MovieCreditsUIModel> = emptyList(),
    val movieReviews: List<MovieReviewsUIModel> = emptyList(),
    val isLoading: Boolean = false,
)
