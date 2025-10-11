package com.alican.domain.repository

import com.alican.domain.models.MovieCredit
import com.alican.domain.models.MovieDetail
import com.alican.domain.models.MovieImages
import com.alican.domain.models.MovieList
import com.alican.domain.models.MovieReviews
import com.alican.domain.utils.Resource

interface MoviesRepository {
    suspend fun getUpComingMovies(page: Int): Resource<MovieList>
    suspend fun getPopularMovies(page: Int): Resource<MovieList>
    suspend fun getTopRatedMovies(page: Int): Resource<MovieList>
    suspend fun getNowPlayingMovies(page: Int): Resource<MovieList>
    suspend fun getMovieDetail(movieId: Int): Resource<MovieDetail>
    suspend fun getMovieCredits(movieId: Int): Resource<MovieCredit>
    suspend fun getMovieReviews(movieId: Int, page: Int): Resource<MovieReviews>
    suspend fun searchMovies(query: String, page: Int): Resource<MovieList>
    suspend fun getMovieImages(id: Int): Resource<MovieImages>
}
