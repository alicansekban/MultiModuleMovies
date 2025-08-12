package com.alican.multimodulemovies.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alican.domain.interactors.MovieListInteractor
import com.alican.domain.models.pagination.PaginationUIModel
import com.alican.multimodulemovies.utils.ScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// Update your MovieListViewModel
@HiltViewModel
class MoviesListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: MovieListInteractor
) : ViewModel() {

    private val movieType = savedStateHandle.toRoute<ScreenRoute.MoviesListRoute>()

    val movies = interactor.paginationState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            PaginationUIModel()
        )

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            interactor.loadMoviesByType(movieType.movieType)
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            interactor.loadNextPage()
        }
    }

    fun retry() {
        viewModelScope.launch {
            interactor.retry()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            interactor.reset()
            interactor.loadFirstPage()
        }
    }
}