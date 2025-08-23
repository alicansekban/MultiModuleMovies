package com.alican.multimodulemovies.ui.list

import com.alican.domain.models.MovieUIModel

sealed interface MovieListUIEvents {
    data object LoadNextPage : MovieListUIEvents
    data object Retry : MovieListUIEvents
    data object Refresh : MovieListUIEvents
    data class OpenMovieDetail(val movieId: Int) : MovieListUIEvents
    data class ToggleFavorite(val movie: MovieUIModel) : MovieListUIEvents // Add this

}

