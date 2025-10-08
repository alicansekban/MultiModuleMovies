
package com.alican.multimodulemovies.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alican.domain.interactors.MovieListInteractor
import com.alican.domain.ui_models.movie.MovieUIModel
import com.alican.domain.ui_models.pagination.PaginationUIModel
import com.alican.multimodulemovies.helpers.navigation.navigateToMovieDetail
import com.alican.multimodulemovies.navigation.AppRouter
import com.alican.multimodulemovies.utils.ScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: MovieListInteractor,
    private val appRouter: AppRouter
) : ViewModel() {

    private val movieType = savedStateHandle.toRoute<ScreenRoute.MoviesListRoute>()

    val movies = interactor.moviesWithFavoriteState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            PaginationUIModel()
        )


    init {
        loadMovies()
    }

    fun onScreenEvent(event: MovieListUIEvents) {
        when (event) {
            MovieListUIEvents.LoadNextPage -> loadNextPage()
            MovieListUIEvents.Retry -> retry()
            MovieListUIEvents.Refresh -> refresh()
            is MovieListUIEvents.OpenMovieDetail -> {
                appRouter.navigateToMovieDetail(event.movieId)
            }

            is MovieListUIEvents.ToggleFavorite -> toggleFavorite(event.movie) // Add this
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            interactor.loadMoviesByType(movieType.movieType)
        }
    }

    private fun loadNextPage() {
        viewModelScope.launch {
            interactor.loadNextPage()
        }
    }

    private fun retry() {
        viewModelScope.launch {
            interactor.retry()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            interactor.reset()
            interactor.loadFirstPage()
        }
    }

    private fun toggleFavorite(movie: MovieUIModel) {
        viewModelScope.launch {
            interactor.toggleFavorite(movie)
        }
    }

}