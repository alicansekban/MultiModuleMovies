package com.alican.multimodulemovies.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alican.domain.interactors.MovieDetailInteractor
import com.alican.domain.models.BaseUIModel
import com.alican.domain.models.MovieCreditsUIModel
import com.alican.domain.models.MovieDetailUIModel
import com.alican.domain.models.MovieReviewsUIModel
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

    private val _movieImages =
        MutableStateFlow<BaseUIModel<List<String>>>(BaseUIModel.Empty)
    val movieImages = _movieImages.onStart {
        getMovieImages(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000L), BaseUIModel.Empty)

    private val _movieCredits =
        MutableStateFlow<BaseUIModel<List<MovieCreditsUIModel>>>(BaseUIModel.Empty)
    val movieCredits = _movieCredits.onStart {
        getMovieCredits(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000L), BaseUIModel.Empty)


    private val _movieReviews =
        MutableStateFlow<BaseUIModel<List<MovieReviewsUIModel>>>(BaseUIModel.Empty)
    val movieReviews = _movieReviews.onStart {
        getMovieReviews(id, 1)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000L), BaseUIModel.Empty)

    private fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            interactor.getMovieDetails(id).collect {
                _movieDetail.emit(it)
            }
        }
    }

    private fun getMovieImages(id: Int) {
        viewModelScope.launch {
            interactor.getMovieImages(id).collect {
                _movieImages.emit(it)
            }
        }
    }

    private fun getMovieCredits(id: Int) {
        viewModelScope.launch {
            interactor.getMovieCredits(id).collect {
                _movieCredits.emit(it)
            }
        }
    }

    private fun getMovieReviews(id: Int, page: Int) {
        viewModelScope.launch {
            interactor.getMovieReviews(id, page).collect {
                _movieReviews.emit(it)
            }
        }
    }
}