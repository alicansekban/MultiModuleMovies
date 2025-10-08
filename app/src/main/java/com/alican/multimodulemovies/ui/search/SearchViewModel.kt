package com.alican.multimodulemovies.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alican.domain.interactors.SearchInteractor
import com.alican.domain.ui_models.movie.MovieUIModel
import com.alican.domain.ui_models.pagination.PaginationUIModel
import com.alican.multimodulemovies.helpers.navigation.navigateToMovieDetail
import com.alican.multimodulemovies.navigation.AppRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val appRouter: AppRouter
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val movies = searchInteractor.moviesWithFavoriteState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            PaginationUIModel()
        )

    init {
        observeSearchQuery()
    }

    fun onScreenEvent(event: SearchUIEvents) {
        when (event) {
            is SearchUIEvents.OnQueryChanged -> updateQuery(event.query)
            is SearchUIEvents.OnMovieClicked -> appRouter.navigateToMovieDetail(event.movieId)
            is SearchUIEvents.OnFavoriteClicked -> toggleFavorite(event.movie)
            SearchUIEvents.OnRetryClicked -> retry()
            SearchUIEvents.OnLoadMore -> loadMore()
            SearchUIEvents.OnClearSearch -> clearSearch()
        }
    }

    private fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    private fun observeSearchQuery() {
        _searchQuery
            .debounce(500) // 500ms debounce
            .distinctUntilChanged()
            .filter { it.isNotBlank() || movies.value.items.isNotEmpty() }
            .onEach { query ->
                if (query.isBlank()) {
                    clearResults()
                } else {
                    searchMovies(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            searchInteractor.searchMovies(query)
        }
    }

    private fun toggleFavorite(movie: MovieUIModel) {
        viewModelScope.launch {
            searchInteractor.toggleFavorite(movie)
        }
    }

    private fun retry() {
        val query = _searchQuery.value
        if (query.isNotBlank()) {
            searchMovies(query)
        }
    }

    private fun loadMore() {
        viewModelScope.launch {
            searchInteractor.loadMoreResults()
        }
    }

    private fun clearSearch() {
        _searchQuery.value = ""
        clearResults()
    }

    private fun clearResults() {
        viewModelScope.launch {
            searchInteractor.reset()
        }
    }
}