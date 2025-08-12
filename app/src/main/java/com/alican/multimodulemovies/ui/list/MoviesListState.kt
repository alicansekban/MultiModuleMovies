package com.alican.multimodulemovies.ui.list

import com.alican.domain.models.MovieListUIModel

sealed interface MovieListUIEvents {
    data object GetNextPage : MovieListUIEvents
}



data class MovieListUIStateModel(
    val uiModel: MovieListUIModel = MovieListUIModel(),
    val isLoading: Boolean = false,
)