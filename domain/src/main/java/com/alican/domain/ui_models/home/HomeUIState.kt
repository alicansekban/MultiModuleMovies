package com.alican.domain.ui_models.home

import com.alican.domain.ui_models.movie.MovieUIModel

data class HomeUIState(
    val upcomingMovies: List<MovieUIModel> = emptyList(),
    val nowPlayingMovies: List<MovieUIModel> = emptyList(),
    val popularMovies: List<MovieUIModel> = emptyList(),
    val topRatedMovies: List<MovieUIModel> = emptyList(),
    val isLoading: Boolean = false,
)