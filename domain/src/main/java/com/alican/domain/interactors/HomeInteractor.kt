package com.alican.domain.interactors

import com.alican.data.data.repository.MoviesRepository
import com.alican.data.utils.ResultWrapper
import com.alican.domain.mappers.toUIModel
import com.alican.domain.models.MovieUIModel
import com.alican.domain.models.home.HomeUIState
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
            is ResultWrapper.Success -> {
                response.value.results?.map { it.toUIModel() } ?: emptyList()
            }

            else -> emptyList()
        }
    }

    private suspend fun getNowPlayingMoviesSync(page: Int): List<MovieUIModel> {
        return when (val response = moviesRepository.getNowPlayingMovies(page)) {
            is ResultWrapper.Success -> {
                response.value.results?.map { it.toUIModel() } ?: emptyList()
            }

            else -> emptyList()
        }
    }

    private suspend fun getTopRatedMoviesSync(page: Int): List<MovieUIModel> {
        return when (val response = moviesRepository.getTopRatedMovies(page)) {
            is ResultWrapper.Success -> {
                response.value.results?.map { it.toUIModel() } ?: emptyList()
            }

            else -> emptyList()
        }
    }

    private suspend fun getPopularMoviesSync(page: Int): List<MovieUIModel> {
        return when (val response = moviesRepository.getPopularMovies(page)) {
            is ResultWrapper.Success -> {
                response.value.results?.map { it.toUIModel() } ?: emptyList()
            }

            else -> emptyList()
        }
    }
}