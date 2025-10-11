package com.alican.domain.ui_models.movie

data class MovieDetailUIModel(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val overview: String = "",
    val duration: String? = null,
    val voteAvg: String? = null,
    val releaseDate: String? = null,
    val isFavorite: Boolean = false
)