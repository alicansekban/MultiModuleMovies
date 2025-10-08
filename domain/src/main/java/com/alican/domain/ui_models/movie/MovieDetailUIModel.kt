package com.alican.domain.ui_models.movie

data class MovieDetailUIModel(
    val id: Int? = 0,
    val title: String? = null,
    val imageUrl: String? = null,
    val overview: String? = null,
    val duration: String? = null,
    val voteAvg: String? = null,
    val releaseDate: String? = null,
    val isFavorite: Boolean = false
)