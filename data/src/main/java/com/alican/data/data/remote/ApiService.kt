package com.alican.data.data.remote

import com.alican.data.data.response.BaseMoviesResponse
import com.alican.data.data.response.MovieCreditResponse
import com.alican.data.data.response.MovieDetailResponse
import com.alican.data.data.response.MovieImagesResponse
import com.alican.data.data.response.MovieReviewResponse
import com.alican.data.utils.ResultWrapper
import com.alican.data.utils.safeCall
import io.ktor.client.HttpClient
import io.ktor.http.appendPathSegments
import javax.inject.Inject

interface ApiService {
    suspend fun getUpComingMovies(page: Int): ResultWrapper<BaseMoviesResponse>
    suspend fun getPopularMovies(page: Int): ResultWrapper<BaseMoviesResponse>
    suspend fun getTopRatedMovies(page: Int): ResultWrapper<BaseMoviesResponse>
    suspend fun getNowPlayingMovies(page: Int): ResultWrapper<BaseMoviesResponse>
    suspend fun getMovieDetail(movieId: Int): ResultWrapper<MovieDetailResponse>
    suspend fun getMovieCredits(movieId: Int): ResultWrapper<MovieCreditResponse>
    suspend fun getMovieReviews(movieId: Int, page: Int): ResultWrapper<MovieReviewResponse>
    suspend fun searchMovies(query: String, page: Int): ResultWrapper<BaseMoviesResponse>
    suspend fun getMovieImages(id: Int): ResultWrapper<MovieImagesResponse>
}

class ApiServiceImpl @Inject constructor(
    private val client: HttpClient
) : ApiService {

    // Already migrated example (shown for consistency)
    override suspend fun getUpComingMovies(page: Int): ResultWrapper<BaseMoviesResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", "upcoming")
                parameters.append("page", page.toString())
            }
        }

    override suspend fun getPopularMovies(page: Int): ResultWrapper<BaseMoviesResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", "popular")
                parameters.append("page", page.toString())
            }
        }

    override suspend fun getTopRatedMovies(page: Int): ResultWrapper<BaseMoviesResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", "top_rated")
                parameters.append("page", page.toString())
            }
        }

    override suspend fun getNowPlayingMovies(page: Int): ResultWrapper<BaseMoviesResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", "now_playing")
                parameters.append("page", page.toString())
            }
        }

    override suspend fun getMovieDetail(movieId: Int): ResultWrapper<MovieDetailResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", movieId.toString())
            }
        }

    override suspend fun getMovieCredits(movieId: Int): ResultWrapper<MovieCreditResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", movieId.toString(), "credits")
            }
        }

    override suspend fun getMovieReviews(
        movieId: Int,
        page: Int
    ): ResultWrapper<MovieReviewResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", movieId.toString(), "reviews")
                parameters.append("page", page.toString())
            }
        }

    override suspend fun searchMovies(
        query: String,
        page: Int
    ): ResultWrapper<BaseMoviesResponse> =
        safeCall(client) {
            url {
                appendPathSegments("search", "movie")
                parameters.append("query", query)
                parameters.append("page", page.toString())
                // parameters.append("include_adult", "false") // uncomment if needed
            }
        }

    override suspend fun getMovieImages(id: Int): ResultWrapper<MovieImagesResponse> =
        safeCall<MovieImagesResponse>(client) {
            url {
                appendPathSegments("movie", id.toString(), "images")
            }
        }
}