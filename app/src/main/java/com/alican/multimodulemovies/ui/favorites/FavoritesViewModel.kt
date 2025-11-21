package com.alican.multimodulemovies.ui.favorites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.FavoritesInteractor
import com.alican.multimodulemovies.helpers.navigation.AppRouter
import com.alican.multimodulemovies.helpers.navigation.navigateToMovieDetail
import com.alican.multimodulemovies.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesInteractor: FavoritesInteractor,
    private val appRouter: AppRouter,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<FavoritesUIState, FavoritesUIEvents, FavoritesUIEffects>(
    savedStateHandle = savedStateHandle
) {

    override fun initialState(savedStateHandle: SavedStateHandle): FavoritesUIState {
        return FavoritesUIState(isLoading = true)
    }

    init {
        observeFavorites()
    }

    override fun handleEvent(event: FavoritesUIEvents) {
        when (event) {
            is FavoritesUIEvents.RemoveFromFavorites -> removeFromFavorites(event.movieId)
            is FavoritesUIEvents.OpenMovieDetail -> appRouter.navigateToMovieDetail(event.movieId)

        }
    }

    private fun observeFavorites() {
        favoritesInteractor.getFavoriteMovies()
            .onEach { favoriteMovies ->
                updateState {
                    copy(
                        favoriteMovies = favoriteMovies,
                        isLoading = false,
                        favoritesCount = favoriteMovies.size
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun removeFromFavorites(movieId: Int) {
        viewModelScope.launch {
            try {
                favoritesInteractor.removeFromFavorites(movieId)
                sendEffect(FavoritesUIEffects.ShowToast("Removed from favorites"))
            } catch (e: Exception) {
                sendEffect(FavoritesUIEffects.ShowError("Failed to remove from favorites"))
            }
        }
    }
}