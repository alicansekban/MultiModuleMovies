package com.alican.domain.models


data class MovieListUIModel(
    val movies: List<MovieUIModel> = emptyList(),
    val page: Int = 1,
    val totalPages: Int = 1,
    val totalResults: Int = 0,
    val canLoadMore: Boolean = false
)
data class MovieUIModel(
    val id : Int? = 0,
    val title: String ? = null,
    val imageUrl: String? = null,
    val overview: String ?= null,
)

enum class MovieType{
    UPCOMING,
    NOW_PLAYING,
    TOP_RATED,
    POPULAR
}

