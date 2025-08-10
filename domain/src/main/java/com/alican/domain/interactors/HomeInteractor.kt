package com.alican.domain.interactors

import com.alican.data.data.repository.MoviesRepository
import com.alican.data.utils.ResultWrapper
import com.alican.domain.mappers.toUIModel
import com.alican.domain.models.BaseUIModel
import com.alican.domain.models.MovieUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeInteractor @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    fun getUpComingMovies(page: Int): Flow<BaseUIModel<List<MovieUIModel>>> {
        return flow {
            emit(BaseUIModel.Loading)
            emit(when (val response = moviesRepository.getUpComingMovies(page)) {
                is ResultWrapper.Error -> {
                    BaseUIModel.Error(response.message ?: "Error")
                }
                ResultWrapper.Loading -> {
                    BaseUIModel.Loading
                }
                is ResultWrapper.Success -> {
                    val uiModel = response.value.results?.map {
                        it.toUIModel()
                    } ?: emptyList()
                    BaseUIModel.Success(uiModel)
                }
            })
        }
    }

    fun getNowPlayingMovies(page: Int): Flow<BaseUIModel<List<MovieUIModel>>> {
        return flow {
            emit(BaseUIModel.Loading)
            emit(when (val response = moviesRepository.getNowPlayingMovies(page)) {
                is ResultWrapper.Error -> {
                    BaseUIModel.Error(response.message ?: "Error")
                }
                ResultWrapper.Loading -> {
                    BaseUIModel.Loading
                }
                is ResultWrapper.Success -> {
                    val uiModel = response.value.results?.map {
                        it.toUIModel()
                    } ?: emptyList()
                    BaseUIModel.Success(uiModel)
                }
            })
        }
    }

    fun getPopularMovies(page: Int): Flow<BaseUIModel<List<MovieUIModel>>> {
        return flow {
            emit(BaseUIModel.Loading)
            emit(when (val response = moviesRepository.getPopularMovies(page)) {
                is ResultWrapper.Error -> {
                    BaseUIModel.Error(response.message ?: "Error")
                }
                ResultWrapper.Loading -> {
                    BaseUIModel.Loading
                }
                is ResultWrapper.Success -> {
                    val uiModel = response.value.results?.map {
                        it.toUIModel()
                    } ?: emptyList()
                    BaseUIModel.Success(uiModel)
                }
            })
        }
    }

    fun getTopRatedMovies(page: Int): Flow<BaseUIModel<List<MovieUIModel>>> {
        return flow {
            emit(BaseUIModel.Loading)
            emit(when (val response = moviesRepository.getTopRatedMovies(page)) {
                is ResultWrapper.Error -> {
                    BaseUIModel.Error(response.message ?: "Error")
                }
                ResultWrapper.Loading -> {
                    BaseUIModel.Loading
                }
                is ResultWrapper.Success -> {
                    val uiModel = response.value.results?.map {
                        it.toUIModel()
                    } ?: emptyList()
                    BaseUIModel.Success(uiModel)
                }
            })
        }
    }
}