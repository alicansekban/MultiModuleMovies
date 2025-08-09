package com.alican.data.service

import com.alican.data.data.remote.ApiService
import com.alican.data.data.response.BaseMoviesResponse
import com.alican.data.data.response.MovieCreditResponse
import com.alican.data.data.response.MovieDetailResponse
import com.alican.data.data.response.MovieImagesResponse
import com.alican.data.data.response.MovieResponse
import com.alican.data.data.response.MovieReviewResponse
import com.alican.data.utils.ResultWrapper


class FakeApiService : ApiService {

    private val sampleMovies = listOf(
        MovieResponse(
            id = 1,
            title = "Test Movie 1",
            overview = "Test overview 1",
            poster_path = "/test1.jpg",
            backdrop_path = "/backdrop1.jpg",
            release_date = "2023-01-01",
            vote_average = 7.5,
            vote_count = 100,
            adult = false,
            original_language = "en",
            original_title = "Test Movie 1",
            popularity = 100.0,
            video = false,
            genre_ids = listOf(1, 2)
        ),
        MovieResponse(
            id = 2,
            title = "Test Movie 2",
            overview = "Test overview 2",
            poster_path = "/test2.jpg",
            backdrop_path = "/backdrop2.jpg",
            release_date = "2023-02-01",
            vote_average = 8.0,
            vote_count = 150,
            adult = false,
            original_language = "en",
            original_title = "Test Movie 2",
            popularity = 150.0,
            video = false,
            genre_ids = listOf(2, 3)
        )
    )

    private fun createBaseMoviesResponse(page: Int = 1) = BaseMoviesResponse(
        page = page,
        results = sampleMovies,
        total_pages = 10,
        total_results = 100
    )

    var shouldReturnError = false
    var errorCode = 500
    var errorMessage = "Test error"

    override suspend fun getUpComingMovies(page: Int): ResultWrapper<BaseMoviesResponse> {
        return if (shouldReturnError) {
            ResultWrapper.GenericError(code = errorCode, error = errorMessage)
        } else {
            ResultWrapper.Success(createBaseMoviesResponse(page))
        }
    }

    override suspend fun getPopularMovies(page: Int): ResultWrapper<BaseMoviesResponse> {
        return if (shouldReturnError) {
            ResultWrapper.GenericError(code = errorCode, error = errorMessage)
        } else {
            ResultWrapper.Success(createBaseMoviesResponse(page))
        }
    }

    override suspend fun getTopRatedMovies(page: Int): ResultWrapper<BaseMoviesResponse> {
        return if (shouldReturnError) {
            ResultWrapper.GenericError(code = errorCode, error = errorMessage)
        } else {
            ResultWrapper.Success(createBaseMoviesResponse(page))
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): ResultWrapper<BaseMoviesResponse> {
        return if (shouldReturnError) {
            ResultWrapper.GenericError(code = errorCode, error = errorMessage)
        } else {
            ResultWrapper.Success(createBaseMoviesResponse(page))
        }
    }

    override suspend fun getMovieDetail(movieId: Int): ResultWrapper<MovieDetailResponse> {
        return if (shouldReturnError) {
            ResultWrapper.GenericError(code = errorCode, error = errorMessage)
        } else {
            val movieDetail = MovieDetailResponse(
                id = movieId,
                title = "Test Movie Detail",
                voteCount = 200,
                runtime = 120,
                adult = false,
                budget = 1000000,
                homepage = "https://example.com",
                originalTitle = "Test Movie Detail",
                popularity = 200.0,
                revenue = 5000000,
                status = "Released",
                tagline = "Test tagline",
                video = false,
                genres = emptyList(),
                productionCountries = emptyList(),
            )
            ResultWrapper.Success(movieDetail)
        }
    }

    override suspend fun getMovieCredits(movieId: Int): ResultWrapper<MovieCreditResponse> {
        return if (shouldReturnError) {
            ResultWrapper.GenericError(code = errorCode, error = errorMessage)
        } else {
            val credits = MovieCreditResponse(
                id = movieId,
                cast = emptyList(),
                crew = emptyList()
            )
            ResultWrapper.Success(credits)
        }
    }

    override suspend fun getMovieReviews(
        movieId: Int,
        page: Int
    ): ResultWrapper<MovieReviewResponse> {
        return if (shouldReturnError) {
            ResultWrapper.GenericError(code = errorCode, error = errorMessage)
        } else {
            val reviews = MovieReviewResponse(
                page = page,
                results = emptyList(),
            )
            ResultWrapper.Success(reviews)
        }
    }

    override suspend fun searchMovies(query: String, page: Int): ResultWrapper<BaseMoviesResponse> {
        return if (shouldReturnError) {
            ResultWrapper.GenericError(code = errorCode, error = errorMessage)
        } else {
            val filteredMovies = if (query.isNotEmpty()) {
                sampleMovies.filter {
                    it.title?.contains(query, ignoreCase = true) == true
                }
            } else {
                sampleMovies
            }

            val searchResponse = BaseMoviesResponse(
                page = page,
                results = filteredMovies,
            )
            ResultWrapper.Success(searchResponse)
        }
    }

    override suspend fun getMovieImages(id: Int): ResultWrapper<MovieImagesResponse> {
        return if (shouldReturnError) {
            ResultWrapper.GenericError(code = errorCode, error = errorMessage)
        } else {
            val images = MovieImagesResponse(
                id = id,
                backdrops = emptyList(),
                logos = emptyList(),
                posters = emptyList()
            )
            ResultWrapper.Success(images)
        }
    }
}