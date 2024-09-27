package com.alican.data.data.remote

import com.alican.data.data.local.response.BasePagingResponse
import com.alican.data.data.local.response.MovieCreditResponse
import com.alican.data.data.local.response.MovieDetailResponse
import com.alican.data.data.local.response.MovieResponse
import com.alican.data.data.local.response.MovieReviewResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {
    // MOVIE LISTS
    @GET("movie/upcoming")
    suspend fun getUpComingMovies(@Query("page") page : Int): BasePagingResponse<MovieResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page : Int): BasePagingResponse<MovieResponse>
    @GET("movie/top_rated")
    suspend fun getTopRatedApi(@Query("page") page: Int): BasePagingResponse<MovieResponse>
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int): BasePagingResponse<MovieResponse>
    @GET("search/movie")
    suspend fun getSearchMovieApi(
        @Query("page") page: Int,
        @Query("query") query: String
    ): BasePagingResponse<MovieResponse>


    @GET("movie/{id}")
    suspend fun getMovieDetail(@Path("id") id : Int) : MovieDetailResponse

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(@Path("id") id : Int, @Query("page") page: Int) : BasePagingResponse<MovieReviewResponse>

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(@Path("id") id : Int) : MovieCreditResponse

}
