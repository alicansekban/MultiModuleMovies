
package com.alican.multimodulemovies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.HomeInteractor
import com.alican.multimodulemovies.helpers.navigation.navigateToMovieDetail
import com.alican.multimodulemovies.helpers.navigation.navigateToMoviesList
import com.alican.multimodulemovies.navigation.AppRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val interactor: HomeInteractor,
    private val appRouter: AppRouter
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        loadAllMovies()
    }

    fun onScreenEvent(event: HomeUIEvents) {
        when (event) {
            HomeUIEvents.Retry -> retry()
            is HomeUIEvents.OpenMovieDetail -> {
                appRouter.navigateToMovieDetail(event.movieId)
            }

            is HomeUIEvents.OpenMovieList -> {
                appRouter.navigateToMoviesList(event.movieType)
            }
        }
    }

    private fun loadAllMovies(page: Int = 1) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val homeData = interactor.getAllHomeMovies(page)

            _uiState.value = homeData
        }
    }

    private fun retry() {
        loadAllMovies()
    }
}