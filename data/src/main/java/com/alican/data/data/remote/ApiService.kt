package com.alican.data.data.remote

import com.alican.data.BuildConfig
import com.alican.data.data.response.BaseMoviesResponse
import com.alican.data.data.response.MovieCreditResponse
import com.alican.data.data.response.MovieDetailResponse
import com.alican.data.utils.Constants
import com.alican.data.utils.ResultWrapper
import com.alican.data.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ApiService @Inject constructor(
    private val client: HttpClient
) {

    suspend fun getUpComingMovies(page: Int) : ResultWrapper<BaseMoviesResponse> =
        safeApiCall(Dispatchers.IO) {
            client.get("${Constants.UP_COMING_ENDPOINT}$page").body()
        }
    suspend fun getPopularMovies(page: Int) : ResultWrapper<BaseMoviesResponse> =
        safeApiCall(Dispatchers.IO) {
            client.get("${Constants.POPULAR_ENDPOINT}$page").body()
        }
    suspend fun getTopRatedMovies(page: Int) : ResultWrapper<BaseMoviesResponse> =
        safeApiCall(Dispatchers.IO) {
            client.get("${Constants.TOP_RATED_ENDPOINT}$page").body()
        }
    suspend fun getNowPlayingMovies(page: Int) : ResultWrapper<BaseMoviesResponse> =
        safeApiCall(Dispatchers.IO) {
            client.get("${Constants.NOW_PLAYING_ENDPOINT}$page").body()
        }
    suspend fun searchMovie(page: Int, query: String) : ResultWrapper<BaseMoviesResponse> =
        safeApiCall(Dispatchers.IO) {
            client.get("${Constants.SEARCH_MOVIE_ENDPOINT}?page=$page&query=$query}").body()
        }

    suspend fun getMovieDetail(id: Int) : ResultWrapper<MovieDetailResponse> =
        safeApiCall(Dispatchers.IO) {
            client.get("movie/$id").body()
        }

//    suspend fun getMovieReviews(id: Int, page: Int) : ResultWrapper<BasePagingResponse<MovieReviewResponse>> =
//        safeApiCall(Dispatchers.IO) {
//            client.get(urlString = BuildConfig.BASE_URL) {
//                url {
//                    appendPathSegments("id", id.toString())
//
//                    // Query param -> ..?type=recreational
//                    parameters.append("page", page.toString())
//                }
//            }.body()
//        }

    suspend fun getMovieCredits(id: Int) : ResultWrapper<MovieCreditResponse> =
        safeApiCall(Dispatchers.IO) {
            client.get(urlString = BuildConfig.BASE_URL) {
                url {
                    appendPathSegments("id", id.toString())
                }
            }.body()
        }

}