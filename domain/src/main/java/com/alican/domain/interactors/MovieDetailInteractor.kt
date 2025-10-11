package com.alican.domain.interactors

import com.alican.domain.BuildConfig
import com.alican.domain.mappers.toUIModel
import com.alican.domain.repository.MoviesRepository
import com.alican.domain.ui_models.movie.MovieCreditsUIModel
import com.alican.domain.ui_models.movie.MovieDetailUIModel
import com.alican.domain.ui_models.movie.MovieReviewsUIModel
import com.alican.domain.ui_models.movie_detail.MovieDetailUIState
import com.alican.domain.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class MovieDetailInteractor @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend fun getAllMovieDetailData(movieId: Int): MovieDetailUIState = coroutineScope {
        val movieDetailDeferred = async { getMovieDetailSync(movieId) }
        val movieImagesDeferred = async { getMovieImagesSync(movieId) }
        val movieCreditsDeferred = async { getMovieCreditsSync(movieId) }
        val movieReviewsDeferred = async { getMovieReviewsSync(movieId, 1) }

        MovieDetailUIState(
            movieDetail = movieDetailDeferred.await(),
            movieImages = movieImagesDeferred.await(),
            movieCredits = movieCreditsDeferred.await(),
            movieReviews = movieReviewsDeferred.await(),
            isLoading = false
        )
    }

    private suspend fun getMovieDetailSync(movieId: Int): MovieDetailUIModel? {
        return when (val response = moviesRepository.getMovieDetail(movieId)) {
            is Resource.Success -> response.value.toUIModel()
            else -> null
        }
    }

    private suspend fun getMovieImagesSync(movieId: Int): List<String> {
        return when (val response = moviesRepository.getMovieImages(movieId)) {
            is Resource.Success -> {
                response.value.posters.filter { it.iso_639_1 == "en" }.take(10)
                    .map { BuildConfig.BASE_POSTER_URL + it.filePath }
            }

            else -> emptyList()
        }
    }

    private suspend fun getMovieCreditsSync(movieId: Int): List<MovieCreditsUIModel> {
        return when (val response = moviesRepository.getMovieCredits(movieId)) {
            is Resource.Success -> {
                response.value.cast.map { it.toUIModel() }
            }

            else -> emptyList()
        }
    }

    private suspend fun getMovieReviewsSync(movieId: Int, page: Int): List<MovieReviewsUIModel> {
        return when (val response = moviesRepository.getMovieReviews(movieId, page)) {
            is Resource.Success -> {
                response.value.results.map { it.toUIModel() }
            }

            else -> emptyList()
        }
    }
}