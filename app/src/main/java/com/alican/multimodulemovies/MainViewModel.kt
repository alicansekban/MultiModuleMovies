package com.alican.multimodulemovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.MovieUIModel
import com.alican.domain.MoviesInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: MoviesInteractor
) : ViewModel(){

    private val _movies = MutableStateFlow<List<MovieUIModel>>(emptyList())
    val movies = _movies.asStateFlow()

    init {
        getMovies()
    }

    private fun getMovies() {
        viewModelScope.launch {
            interactor.getMovies(1).collect{
                _movies.value = it
            }
        }
    }
}