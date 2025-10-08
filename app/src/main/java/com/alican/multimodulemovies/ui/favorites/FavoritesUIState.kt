package com.alican.multimodulemovies.ui.favorites

import com.alican.domain.ui_models.movie.MovieUIModel

data class FavoritesUIState(
    val favoriteMovies: List<MovieUIModel> = emptyList(),
    val isLoading: Boolean = false,
    val favoritesCount: Int = 0
) {
    val isEmpty: Boolean
        get() = favoriteMovies.isEmpty() && !isLoading
}

sealed interface FavoritesUIEvents {
    data class RemoveFromFavorites(val movieId: Int) : FavoritesUIEvents
    data class OpenMovieDetail(val movieId: Int) : FavoritesUIEvents
}

sealed interface FavoritesUIEffects {
    data class ShowError(val message: String) : FavoritesUIEffects
    data class ShowToast(val message: String) : FavoritesUIEffects
}
