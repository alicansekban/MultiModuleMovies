package com.alican.multimodulemovies.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alican.domain.interactors.MovieDetailInteractor
import com.alican.domain.models.BaseUIModel
import com.alican.domain.models.MovieDetailUIModel
import com.alican.multimodulemovies.utils.MovieDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: MovieDetailInteractor
) : ViewModel() {

    private val id = savedStateHandle.toRoute<MovieDetailRoute>().movieId

    private val _movieDetail =
        MutableStateFlow<BaseUIModel<MovieDetailUIModel>>(BaseUIModel.Empty)
    val movieDetail = _movieDetail.onStart {
        getMovieDetail(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000L), BaseUIModel.Empty)

    private fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            interactor.getMovieDetails(id).collect {
                _movieDetail.emit(it)
            }
        }
    }
}