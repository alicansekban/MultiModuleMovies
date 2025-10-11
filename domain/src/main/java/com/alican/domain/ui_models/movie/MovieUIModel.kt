package com.alican.domain.ui_models.movie

data class MovieListUIModel(
    val movies: List<MovieUIModel> = emptyList(),
    val page: Int = 1,
    val totalPages: Int = 1,
    val totalResults: Int = 0,
    val canLoadMore: Boolean = false
)

data class MovieUIModel(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val overview: String = "",
    val isFavorite: Boolean = false
)

enum class MovieType {
    UPCOMING,
    NOW_PLAYING,
    TOP_RATED,
    POPULAR
}