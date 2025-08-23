package com.alican.multimodulemovies.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alican.domain.interactors.FavoritesInteractor
import com.alican.domain.interactors.MovieDetailInteractor
import com.alican.domain.models.MovieUIModel
import com.alican.domain.models.movie_detail.MovieDetailUIState
import com.alican.multimodulemovies.utils.ScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: MovieDetailInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val id = savedStateHandle.toRoute<ScreenRoute.MovieDetailRoute>().movieId

    private val _uiState = MutableStateFlow(MovieDetailUIState())
    val uiState: StateFlow<MovieDetailUIState> = _uiState.asStateFlow()

    init {
        loadAllMovieDetailData()
    }

    fun onScreenEvent(event: MovieDetailUIEvents) {
        when (event) {
            MovieDetailUIEvents.Retry -> retry()
            MovieDetailUIEvents.GoBack -> {
                // This will be handled by the screen composable
            }

            MovieDetailUIEvents.ToggleFavorite -> toggleFavorite()
            MovieDetailUIEvents.ShareMovie -> {
                // This will be handled by the screen composable
            }

            is MovieDetailUIEvents.OpenTrailer -> {
                // This will be handled by the screen composable
            }

            is MovieDetailUIEvents.OpenActorProfile -> {
                // This will be handled by the screen composable
            }
        }
    }

    private fun loadAllMovieDetailData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val detailData = interactor.getAllMovieDetailData(id)

            _uiState.value = detailData
            collectFavoriteState(id)
        }
    }

    private fun retry() {
        loadAllMovieDetailData()
    }

    private fun toggleFavorite() {
        val movie = uiState.value.movieDetail ?: return
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
                _uiState.update {
                    it.copy(
                        movieDetail = it.movieDetail?.copy(isFavorite = isFavorite)
                            ?: it.movieDetail,
                    )
                }
            }
        }
    }
}