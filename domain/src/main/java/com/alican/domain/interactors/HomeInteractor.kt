package com.alican.domain.interactors

import com.alican.domain.mappers.toUIModel
import com.alican.domain.repository.MoviesRepository
import com.alican.domain.ui_models.home.HomeUIState
import com.alican.domain.ui_models.movie.MovieUIModel
import com.alican.domain.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class HomeInteractor @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend fun getAllHomeMovies(page: Int = 1): HomeUIState = coroutineScope {
        val upcomingDeferred = async { getUpcomingMoviesSync(page) }
        val nowPlayingDeferred = async { getNowPlayingMoviesSync(page) }
        val topRatedDeferred = async { getTopRatedMoviesSync(page) }
        val popularDeferred = async { getPopularMoviesSync(page) }

        HomeUIState(
            upcomingMovies = upcomingDeferred.await(),
            nowPlayingMovies = nowPlayingDeferred.await(),
            topRatedMovies = topRatedDeferred.await(),
            popularMovies = popularDeferred.await(),
            isLoading = false
        )
    }

    private suspend fun getUpcomingMoviesSync(page: Int): List<MovieUIModel> {
        return when (val response = moviesRepository.getUpComingMovies(page)) {
            is Resource.Success -> {
                response.value.results.map { it.toUIModel() }
            }

            else -> emptyList()
        }
    }

    private suspend fun getNowPlayingMoviesSync(page: Int): List<MovieUIModel> {
        return when (val response = moviesRepository.getNowPlayingMovies(page)) {
            is Resource.Success -> {
                response.value.results.map { it.toUIModel() }
            }

            else -> emptyList()
        }
    }

    private suspend fun getTopRatedMoviesSync(page: Int): List<MovieUIModel> {
        return when (val response = moviesRepository.getTopRatedMovies(page)) {
            is Resource.Success -> {
                response.value.results.map { it.toUIModel() }
            }

            else -> emptyList()
        }
    }

    private suspend fun getPopularMoviesSync(page: Int): List<MovieUIModel> {
        return when (val response = moviesRepository.getPopularMovies(page)) {
            is Resource.Success -> {
                response.value.results.map { it.toUIModel() }
            }

            else -> emptyList()
        }
    }
}