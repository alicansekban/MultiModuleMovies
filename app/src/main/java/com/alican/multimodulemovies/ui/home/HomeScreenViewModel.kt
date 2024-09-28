package com.alican.multimodulemovies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.HomeInteractor
import com.alican.domain.models.BaseUIModel
import com.alican.domain.models.MovieUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val interactor: HomeInteractor
) : ViewModel() {

    private val _upComingMovies =
        MutableStateFlow<BaseUIModel<List<MovieUIModel>>>(BaseUIModel.Empty)
    val upComingMovies =
        _upComingMovies.onStart {
            getUpComingMovies()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000L), BaseUIModel.Empty)

    private val _nowPlayingMovies =
        MutableStateFlow<BaseUIModel<List<MovieUIModel>>>(BaseUIModel.Empty)
    val nowPlayingMovies = _nowPlayingMovies.onStart {
        getNowPlayingMovies()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10000L),
        BaseUIModel.Empty
    )

    private fun getUpComingMovies() {
        viewModelScope.launch {
            interactor.getUpComingMovies(1).collect { state ->
                _upComingMovies.emit(state)
            }
        }
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            interactor.getNowPlayingMovies(1).collect { state ->
                _nowPlayingMovies.emit(state)
            }
        }
    }
}