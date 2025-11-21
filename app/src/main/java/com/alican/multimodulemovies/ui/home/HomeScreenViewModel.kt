
package com.alican.multimodulemovies.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.HomeInteractor
import com.alican.multimodulemovies.helpers.navigation3.AppRouter
import com.alican.multimodulemovies.helpers.navigation3.navigateToMovieDetail
import com.alican.multimodulemovies.helpers.navigation3.navigateToMoviesList
import com.alican.multimodulemovies.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val interactor: HomeInteractor,
    private val appRouter: AppRouter,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<HomeUIState, HomeUIEvents, HomeUIEffects>(
    savedStateHandle = savedStateHandle
) {

    override fun handleEvent(event: HomeUIEvents) {
        when (event) {
            is HomeUIEvents.OpenMovieDetail -> appRouter.navigateToMovieDetail(event.movieId)
            is HomeUIEvents.OpenMovieList -> appRouter.navigateToMoviesList(event.movieType)
            HomeUIEvents.Retry -> retry()
        }
    }

    override fun initialState(savedStateHandle: SavedStateHandle): HomeUIState {
        return HomeUIState()
    }

    init {
        loadAllMovies()
    }

    private fun loadAllMovies(page: Int = 1) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val homeData = interactor.getAllHomeMovies(page)
            updateState { homeData }
        }
    }

    private fun retry() {
        loadAllMovies()
    }
}