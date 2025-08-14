package com.alican.multimodulemovies.ui.list

sealed interface MovieListUIEvents {
    data object LoadNextPage : MovieListUIEvents
    data object Retry : MovieListUIEvents
    data object Refresh : MovieListUIEvents
    data class OpenMovieDetail(val movieId: Int) : MovieListUIEvents
}

