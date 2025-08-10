package com.alican.domain.interactors

import com.alican.data.data.repository.MoviesRepository
import com.alican.data.utils.ResultWrapper
import com.alican.domain.mappers.toUIModel
import com.alican.domain.models.BaseUIModel
import com.alican.domain.models.MovieListUIModel
import com.alican.domain.models.MovieType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieListInteractor @Inject constructor(
    private val repository: MoviesRepository
) {

    fun getMoviesByType(
        movieType: MovieType,
        page: Int,
        currentModel: MovieListUIModel
    ): Flow<BaseUIModel<MovieListUIModel>> {
        return flow {
            when (movieType) {
                MovieType.UPCOMING -> {
                    emitAll(getUpComingMovies(page, currentModel))
                }

                MovieType.NOW_PLAYING -> {
                    emitAll(getNowPlayingMovies(page, currentModel))
                }

                MovieType.TOP_RATED -> {
                    emitAll(getTopRatedMovies(page, currentModel))
                }
                MovieType.POPULAR -> {
                    emitAll(getPopularMovies(page, currentModel))
                }
            }
        }
    }

    private fun getUpComingMovies(
        page: Int,
        currentModel: MovieListUIModel
    ): Flow<BaseUIModel<MovieListUIModel>> {
        return flow {
            emit(BaseUIModel.Loading)
            emit(
                when (val response = repository.getUpComingMovies(page)) {
                    is ResultWrapper.Error -> {
                        BaseUIModel.Error(response.message ?: "Error")
                    }

                    ResultWrapper.Loading -> {
                        BaseUIModel.Loading
                    }

                    is ResultWrapper.Success -> {
                        val uiModel = response.value.toUIModel(currentModel)
                        BaseUIModel.Success(uiModel)
                    }
                }
            )
        }
    }

    private fun getNowPlayingMovies(
        page: Int,
        currentModel: MovieListUIModel
    ): Flow<BaseUIModel<MovieListUIModel>> {
        return flow {

            emit(BaseUIModel.Loading)
            emit(
                when (val response = repository.getNowPlayingMovies(page)) {
                    is ResultWrapper.Error -> {
                        BaseUIModel.Error(response.message ?: "Error")
                    }

                    ResultWrapper.Loading -> {
                        BaseUIModel.Loading
                    }

                    is ResultWrapper.Success -> {
                        val uiModel = response.value.toUIModel(currentModel)
                        BaseUIModel.Success(uiModel)
                    }
                }
            )
        }
    }

    private fun getTopRatedMovies(
        page: Int,
        currentModel: MovieListUIModel
    ): Flow<BaseUIModel<MovieListUIModel>> {
        return flow {

            emit(BaseUIModel.Loading)
            emit(
                when (val response = repository.getTopRatedMovies(page)) {
                    is ResultWrapper.Error -> {
                        BaseUIModel.Error(response.message ?: "Error")
                    }

                    ResultWrapper.Loading -> {
                        BaseUIModel.Loading
                    }

                    is ResultWrapper.Success -> {
                        val uiModel = response.value.toUIModel(currentModel)
                        BaseUIModel.Success(uiModel)
                    }
                }
            )
        }
    }

    private fun getPopularMovies(
        page: Int,
        currentModel: MovieListUIModel
    ): Flow<BaseUIModel<MovieListUIModel>> {
        return flow {

            emit(BaseUIModel.Loading)
            emit(
                when (val response = repository.getPopularMovies(page)) {
                    is ResultWrapper.Error -> {
                        BaseUIModel.Error(response.message ?: "Error")
                    }

                    ResultWrapper.Loading -> {
                        BaseUIModel.Loading
                    }

                    is ResultWrapper.Success -> {
                        val uiModel = response.value.toUIModel(currentModel)
                        BaseUIModel.Success(uiModel)
                    }
                }
            )
        }
    }
}