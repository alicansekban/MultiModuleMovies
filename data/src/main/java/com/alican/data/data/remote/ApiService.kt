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

class ApiService @Inject constructor(
    private val client: HttpClient
) {

    // Already migrated example (shown for consistency)
    suspend fun getUpComingMovies(page: Int): ResultWrapper<BaseMoviesResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", "upcoming")
                parameters.append("page", page.toString())
            }
        }

    suspend fun getPopularMovies(page: Int): ResultWrapper<BaseMoviesResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", "popular")
                parameters.append("page", page.toString())
            }
        }

    suspend fun getTopRatedMovies(page: Int): ResultWrapper<BaseMoviesResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", "top_rated")
                parameters.append("page", page.toString())
            }
        }

    suspend fun getNowPlayingMovies(page: Int): ResultWrapper<BaseMoviesResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", "now_playing")
                parameters.append("page", page.toString())
            }
        }

    suspend fun getMovieDetail(movieId: Int): ResultWrapper<MovieDetailResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", movieId.toString())
            }
        }

    suspend fun getMovieCredits(movieId: Int): ResultWrapper<MovieCreditResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", movieId.toString(), "credits")
            }
        }

    suspend fun getMovieReviews(
        movieId: Int,
        page: Int
    ): ResultWrapper<MovieReviewResponse> =
        safeCall(client) {
            url {
                appendPathSegments("movie", movieId.toString(), "reviews")
                parameters.append("page", page.toString())
            }
        }

    suspend fun searchMovies(
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

    suspend fun getMovieImages(id: Int): ResultWrapper<MovieImagesResponse> =
        safeCall<MovieImagesResponse>(client) {
            url {
                appendPathSegments("movie", id.toString(), "images")
            }
        }

    // Add more endpoints following the same pattern as needed.
}