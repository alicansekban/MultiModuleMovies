package com.alican.data.data.repository

import com.alican.data.data.remote.ApiService
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getUpComingMovies(page: Int) = apiService.getUpComingMovies(page)
    suspend fun getNowPlayingMovies(page: Int) = apiService.getNowPlayingMovies(page)
    suspend fun getPopularMovies(page: Int) = apiService.getPopularMovies(page)
    suspend fun getTopRatedMovies(page: Int) = apiService.getTopRatedMovies(page)
    suspend fun getMovieDetails(id: Int) = apiService.getMovieDetail(id)
    suspend fun getMovieImages(id: Int) = apiService.getMovieImages(id)
}