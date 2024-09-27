package com.alican.multimodulemovies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.models.MovieUIModel
import com.alican.domain.interactors.MoviesInteractor
import com.alican.domain.models.BaseUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val interactor: MoviesInteractor
) :ViewModel(){

    private val _upComingMovies = MutableStateFlow<BaseUIModel<List<MovieUIModel>>>(BaseUIModel.Empty)
    val upComingMovies = _upComingMovies.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), BaseUIModel.Empty)

    init {
        getUpComingMovies()
    }

    private fun getUpComingMovies() {
        viewModelScope.launch {
            interactor.getUpComingMovies(1).collect{state ->
                _upComingMovies.emit(state)
            }
        }
    }
}