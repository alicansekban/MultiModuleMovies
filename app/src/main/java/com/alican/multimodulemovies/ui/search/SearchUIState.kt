package com.alican.multimodulemovies.ui.search

import com.alican.domain.ui_models.movie.MovieUIModel
import com.alican.domain.ui_models.pagination.PaginationUIModel

data class SearchUIState(
    val searchQuery: String = "",
    val searchResults: PaginationUIModel<MovieUIModel> = PaginationUIModel(),
    val favoriteMovieIds: Set<Int> = emptySet(),
    val isSearching: Boolean = false
)

sealed interface SearchUIEvents {
    data class OnQueryChanged(val query: String) : SearchUIEvents
    data class OnMovieClicked(val movieId: Int) : SearchUIEvents
    data class OnFavoriteClicked(val movie: MovieUIModel) : SearchUIEvents
    object OnRetryClicked : SearchUIEvents
    object OnLoadMore : SearchUIEvents
    object OnClearSearch : SearchUIEvents
}

sealed interface SearchUIEffects {
    data class ShowToast(val message: String) : SearchUIEffects
    data class ShowError(val message: String) : SearchUIEffects
}