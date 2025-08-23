
package com.alican.multimodulemovies.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.FavoritesInteractor
import com.alican.multimodulemovies.helpers.navigation.navigateToMovieDetail
import com.alican.multimodulemovies.navigation.AppRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesInteractor: FavoritesInteractor,
    private val appRouter: AppRouter
) : ViewModel() {

    val uiState = combine(
        favoritesInteractor.getFavoriteMovies(),
        favoritesInteractor.getFavoritesCount()
    ) { favoriteMovies, favoritesCount ->
        FavoritesUIState(
            favoriteMovies = favoriteMovies,
            favoritesCount = favoritesCount,
            isLoading = false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        FavoritesUIState(isLoading = true)
    )

    fun onEvent(event: FavoritesUIEvents) {
        when (event) {
            is FavoritesUIEvents.RemoveFromFavorites -> {
                removeFromFavorites(event.movieId)
            }

            is FavoritesUIEvents.OpenMovieDetail -> {
                appRouter.navigateToMovieDetail(event.movieId)
            }
        }
    }

    private fun removeFromFavorites(movieId: Int) {
        viewModelScope.launch {
            favoritesInteractor.removeFromFavorites(movieId)
        }
    }
}