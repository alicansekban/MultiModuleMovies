package com.alican.domain.interactors

import com.alican.data.BuildConfig
import com.alican.data.data.repository.MoviesRepository
import com.alican.data.utils.ResultWrapper
import com.alican.domain.mappers.toUIModel
import com.alican.domain.models.MovieCreditsUIModel
import com.alican.domain.models.MovieDetailUIModel
import com.alican.domain.models.MovieReviewsUIModel
import com.alican.domain.models.movie_detail.MovieDetailUIState
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
        return when (val response = moviesRepository.getMovieDetails(movieId)) {
            is ResultWrapper.Success -> response.value.toUIModel()
            else -> null
        }
    }

    private suspend fun getMovieImagesSync(movieId: Int): List<String> {
        return when (val response = moviesRepository.getMovieImages(movieId)) {
            is ResultWrapper.Success -> {
                response.value.posters?.filter { it.iso_639_1 == "en" }?.take(10)
                    ?.map { BuildConfig.BASE_POSTER_URL + it.file_path.orEmpty() }
                    ?: emptyList()
            }

            else -> emptyList()
        }
    }

    private suspend fun getMovieCreditsSync(movieId: Int): List<MovieCreditsUIModel> {
        return when (val response = moviesRepository.getMovieCredits(movieId)) {
            is ResultWrapper.Success -> {
                response.value.cast?.map { it.toUIModel() } ?: emptyList()
            }

            else -> emptyList()
        }
    }

    private suspend fun getMovieReviewsSync(movieId: Int, page: Int): List<MovieReviewsUIModel> {
        return when (val response = moviesRepository.getMovieReviews(movieId, page)) {
            is ResultWrapper.Success -> {
                response.value.results?.map { it.toUIModel() } ?: emptyList()
            }

            else -> emptyList()
        }
    }
}