package com.alican.data.data.repository

import com.alican.data.data.remote.ApiService
import com.alican.data.mappers.toDomainModel
import com.alican.data.utils.ResultWrapper
import com.alican.domain.models.MovieCredit
import com.alican.domain.models.MovieDetail
import com.alican.domain.models.MovieImages
import com.alican.domain.models.MovieList
import com.alican.domain.models.MovieReviews
import com.alican.domain.repository.MoviesRepository
import com.alican.domain.utils.Resource
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    MoviesRepository {

    override suspend fun getUpComingMovies(page: Int): Resource<MovieList> {
        return when (val response = apiService.getUpComingMovies(page)) {
            is ResultWrapper.Success -> Resource.Success(response.value.toDomainModel())
            is ResultWrapper.Error -> Resource.Error(response.message)
            else -> Resource.Error("Unknown error")
        }
    }

    override suspend fun getPopularMovies(page: Int): Resource<MovieList> {
        return when (val response = apiService.getPopularMovies(page)) {
            is ResultWrapper.Success -> Resource.Success(response.value.toDomainModel())
            is ResultWrapper.Error -> Resource.Error(response.message)
            else -> Resource.Error("Unknown error")
        }
    }

    override suspend fun getTopRatedMovies(page: Int): Resource<MovieList> {
        return when (val response = apiService.getTopRatedMovies(page)) {
            is ResultWrapper.Success -> Resource.Success(response.value.toDomainModel())
            is ResultWrapper.Error -> Resource.Error(response.message)
            else -> Resource.Error("Unknown error")
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): Resource<MovieList> {
        return when (val response = apiService.getNowPlayingMovies(page)) {
            is ResultWrapper.Success -> Resource.Success(response.value.toDomainModel())
            is ResultWrapper.Error -> Resource.Error(response.message)
            else -> Resource.Error("Unknown error")
        }
    }

    override suspend fun getMovieDetail(movieId: Int): Resource<MovieDetail> {
        return when (val response = apiService.getMovieDetail(movieId)) {
            is ResultWrapper.Success -> Resource.Success(response.value.toDomainModel())
            is ResultWrapper.Error -> Resource.Error(response.message)
            else -> Resource.Error("Unknown error")
        }
    }

    override suspend fun getMovieCredits(movieId: Int): Resource<MovieCredit> {
        return when (val response = apiService.getMovieCredits(movieId)) {
            is ResultWrapper.Success -> Resource.Success(response.value.toDomainModel())
            is ResultWrapper.Error -> Resource.Error(response.message)
            else -> Resource.Error("Unknown error")
        }
    }

    override suspend fun getMovieReviews(movieId: Int, page: Int): Resource<MovieReviews> {
        return when (val response = apiService.getMovieReviews(movieId, page)) {
            is ResultWrapper.Success -> Resource.Success(response.value.toDomainModel())
            is ResultWrapper.Error -> Resource.Error(response.message)
            else -> Resource.Error("Unknown error")
        }
    }

    override suspend fun searchMovies(query: String, page: Int): Resource<MovieList> {
        return when (val response = apiService.searchMovies(query, page)) {
            is ResultWrapper.Success -> Resource.Success(response.value.toDomainModel())
            is ResultWrapper.Error -> Resource.Error(response.message)
            else -> Resource.Error("Unknown error")
        }
    }

    override suspend fun getMovieImages(id: Int): Resource<MovieImages> {
        return when (val response = apiService.getMovieImages(id)) {
            is ResultWrapper.Success -> Resource.Success(response.value.toDomainModel())
            is ResultWrapper.Error -> Resource.Error(response.message)
            else -> Resource.Error("Unknown error")
        }
    }
}
