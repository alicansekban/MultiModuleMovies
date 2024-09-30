package com.alican.multimodulemovies.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alican.domain.interactors.MovieListInteractor
import com.alican.domain.models.BaseUIModel
import com.alican.domain.models.MovieListUIModel
import com.alican.domain.models.MovieType
import com.alican.multimodulemovies.utils.MoviesListRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: MovieListInteractor
) : ViewModel() {

    private val type = savedStateHandle.toRoute<MoviesListRoute>()

    private val _movies =
        MutableStateFlow(MovieListUIStateModel())
    val movies = _movies.onStart {
        getMoviesByType(type.movieType, 1,MovieListUIModel())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10000L),
        MovieListUIStateModel()
    )
    fun updateEvents(event: MovieListUIEvents) {
        when (event) {
            MovieListUIEvents.GetNextPage -> {
                getMoviesByType(page = _movies.value.uiModel.page.plus(1), currentModel = _movies.value.uiModel)
            }
        }
    }

    private fun getMoviesByType(movieType: MovieType = type.movieType, page: Int,currentModel : MovieListUIModel) {
        viewModelScope.launch {
            interactor.getMoviesByType(movieType = movieType,
                page = page,
                currentModel = currentModel
            ).collect { state ->
                if (state is BaseUIModel.Success) {
                    _movies.value = _movies.value.copy(uiModel = state.data)
                }
                _movies.value = _movies.value.copy(isLoading = state is BaseUIModel.Loading)

            }
        }
    }
}