package com.alican.multimodulemovies.ui.detail


sealed interface MovieDetailUIEvents {
    data object Retry : MovieDetailUIEvents
    data object GoBack : MovieDetailUIEvents
    data object ToggleFavorite : MovieDetailUIEvents
    data object ShareMovie : MovieDetailUIEvents
    data class OpenTrailer(val trailerUrl: String) : MovieDetailUIEvents
    data class OpenActorProfile(val actorId: Int) : MovieDetailUIEvents
}
