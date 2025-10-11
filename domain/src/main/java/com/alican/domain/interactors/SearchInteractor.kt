package com.alican.domain.interactors

import com.alican.domain.mappers.toUIModel
import com.alican.domain.repository.MoviesRepository
import com.alican.domain.ui_models.BaseUIModel
import com.alican.domain.ui_models.movie.MovieListUIModel
import com.alican.domain.ui_models.movie.MovieUIModel
import com.alican.domain.ui_models.pagination.PaginationUIModel
import com.alican.domain.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SearchInteractor @Inject constructor(
    private val moviesRepository: MoviesRepository,
    override val coroutineScope: CoroutineScope,
    private val favoritesInteractor: FavoritesInteractor
) : BasePaginatedInteractor<MovieUIModel, MovieListUIModel>() {

    val moviesWithFavoriteState: Flow<PaginationUIModel<MovieUIModel>> =
        paginationState.combine(favoritesInteractor.getFavoriteMovieIds()) { pagination, favoriteIds ->
            pagination.copy(
                items = pagination.items.map { movie ->
                    movie.copy(isFavorite = favoriteIds.contains(movie.id))
                }
            )
        }

    private var currentQuery: String = ""

    suspend fun searchMovies(query: String) {
        if (query.isBlank()) {
            reset()
            return
        }

        if (query != currentQuery) {
            currentQuery = query
            reset()
        }
        loadFirstPage()
    }

    suspend fun loadMoreResults() {
        if (currentQuery.isNotBlank()) {
            loadNextPage()
        }
    }

    override suspend fun fetchData(page: Int): BaseUIModel<MovieListUIModel> {
        paginationManager.setLoading(page == 1)
        return moviesRepository.searchMovies(query = currentQuery, page = page).let { result ->
            when (result) {
                is Resource.Error -> BaseUIModel.Error(result.message.orEmpty())
                is Resource.Success -> {
                    val currentState = paginationManager.state.value
                    val existingMovies = if (page == 1) emptyList() else currentState.items
                    val currentModel = MovieListUIModel(
                        movies = existingMovies,
                        page = currentState.currentPage,
                        totalPages = currentState.totalPages,
                        totalResults = currentState.totalResults,
                        canLoadMore = currentState.canLoadMore
                    )
                    BaseUIModel.Success(result.value.toUIModel(currentModel))
                }
            }
        }
    }

    override fun mapToUIModel(data: MovieListUIModel): List<MovieUIModel> {
        return data.movies
    }

    override fun getTotalPages(data: MovieListUIModel): Int {
        return data.totalPages
    }

    override fun getTotalResults(data: MovieListUIModel): Int {
        return data.totalResults
    }

    suspend fun toggleFavorite(movie: MovieUIModel) {
        favoritesInteractor.toggleFavorite(movie)
    }

}