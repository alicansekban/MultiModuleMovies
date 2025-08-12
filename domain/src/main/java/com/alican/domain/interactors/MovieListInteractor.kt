package com.alican.domain.interactors

import com.alican.data.data.repository.MoviesRepository
import com.alican.data.utils.ResultWrapper
import com.alican.domain.mappers.toUIModel
import com.alican.domain.models.MovieListUIModel
import com.alican.domain.models.MovieType
import com.alican.domain.models.MovieUIModel
import javax.inject.Inject


// Update your MovieListInteractor to use the new pagination system
class MovieListInteractor @Inject constructor(
    private val repository: MoviesRepository
) : BasePaginatedInteractor<MovieUIModel, MovieListUIModel>() {

    private var currentMovieType: MovieType = MovieType.POPULAR

    override suspend fun fetchData(page: Int): ResultWrapper<MovieListUIModel> {
        return when (currentMovieType) {
            MovieType.UPCOMING -> repository.getUpComingMovies(page)
            MovieType.NOW_PLAYING -> repository.getNowPlayingMovies(page)
            MovieType.TOP_RATED -> repository.getTopRatedMovies(page)
            MovieType.POPULAR -> repository.getPopularMovies(page)
        }.let { result ->
            when (result) {
                is ResultWrapper.Success -> ResultWrapper.Success(
                    result.value.toUIModel(
                        MovieListUIModel()
                    )
                )

                is ResultWrapper.Error -> result
                ResultWrapper.Loading -> ResultWrapper.Loading
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

    suspend fun loadMoviesByType(movieType: MovieType) {
        if (currentMovieType != movieType) {
            currentMovieType = movieType
            reset()
            loadFirstPage()
        }
    }
}