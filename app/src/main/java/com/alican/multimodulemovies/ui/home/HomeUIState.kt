package com.alican.multimodulemovies.ui.home

import com.alican.domain.ui_models.movie.MovieType
import com.alican.domain.ui_models.home.HomeUIState as DomainHomeUIState

// Using the domain HomeUIState as base but we can extend it if needed for UI-specific state
typealias HomeUIState = DomainHomeUIState

sealed interface HomeUIEvents {
    data object Retry : HomeUIEvents
    data class OpenMovieDetail(val movieId: Int) : HomeUIEvents
    data class OpenMovieList(val movieType: MovieType) : HomeUIEvents
}

sealed interface HomeUIEffects {

}