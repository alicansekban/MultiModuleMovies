package com.alican.multimodulemovies.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.FavoritesInteractor
import com.alican.domain.interactors.MovieDetailInteractor
import com.alican.domain.ui_models.movie.MovieUIModel
import com.alican.domain.ui_models.movie_detail.MovieDetailUIState
import com.alican.multimodulemovies.helpers.navigation3.entry.EntryRoutes
import com.alican.multimodulemovies.utils.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


@HiltViewModel(assistedFactory = MovieDetailViewModel.Factory::class)
class MovieDetailViewModel @AssistedInject constructor(
    @Assisted val navKey: EntryRoutes.MovieDetailRoute,
    private val interactor: MovieDetailInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieDetailUIState, MovieDetailUIEvents, MovieDetailUIEffects>(
    savedStateHandle = savedStateHandle
) {

    @AssistedFactory
    interface Factory {
        fun create(navKey: EntryRoutes.MovieDetailRoute): MovieDetailViewModel
    }

    init {
        loadAllMovieDetailData()
    }

    override fun initialState(savedStateHandle: SavedStateHandle): MovieDetailUIState =
        MovieDetailUIState()

    override fun handleEvent(event: MovieDetailUIEvents) {
        when (event) {
            MovieDetailUIEvents.Retry -> retry()
            MovieDetailUIEvents.ToggleFavorite -> toggleFavorite()
            // Navigation/share are handled by the screen composable
            MovieDetailUIEvents.GoBack,
            MovieDetailUIEvents.ShareMovie,
            is MovieDetailUIEvents.OpenTrailer,
            is MovieDetailUIEvents.OpenActorProfile -> Unit
        }
    }

    private fun loadAllMovieDetailData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val detailData = interactor.getAllMovieDetailData(navKey.movieId)

            updateState { detailData }
            collectFavoriteState(navKey.movieId)
        }
    }

    private fun retry() {
        loadAllMovieDetailData()
    }

    private fun toggleFavorite() {
        val movie = currentState.movieDetail ?: return
        viewModelScope.launch {
            favoritesInteractor.toggleFavorite(
                movie = MovieUIModel(
                    id = movie.id,
                    title = movie.title,
                    overview = movie.overview,
                    imageUrl = movie.imageUrl,
                    isFavorite = movie.isFavorite,
                )
            )
        }
    }

    private fun collectFavoriteState(id: Int) {
        viewModelScope.launch {
            favoritesInteractor.isMovieFavorite(id).collect { isFavorite ->
                updateState {
                    copy(
                        movieDetail = movieDetail?.copy(isFavorite = isFavorite) ?: movieDetail,
                    )
                }
            }
        }
    }
}
