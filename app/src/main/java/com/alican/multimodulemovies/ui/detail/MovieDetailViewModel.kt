package com.alican.multimodulemovies.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alican.domain.interactors.MovieDetailInteractor
import com.alican.domain.models.movie_detail.MovieDetailUIState
import com.alican.multimodulemovies.utils.ScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: MovieDetailInteractor
) : ViewModel() {

    private val id = savedStateHandle.toRoute<ScreenRoute.MovieDetailRoute>().movieId

    private val _uiState = MutableStateFlow(MovieDetailUIState())
    val uiState: StateFlow<MovieDetailUIState> = _uiState.asStateFlow()

    init {
        loadAllMovieDetailData()
    }

    private fun loadAllMovieDetailData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val detailData = interactor.getAllMovieDetailData(id)

            _uiState.value = detailData
        }
    }

    fun retry() {
        loadAllMovieDetailData()
    }
}