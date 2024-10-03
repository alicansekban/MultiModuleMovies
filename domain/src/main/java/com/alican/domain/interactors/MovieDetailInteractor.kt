package com.alican.domain.interactors

import com.alican.data.BuildConfig
import com.alican.data.data.repository.MoviesRepository
import com.alican.data.utils.ResultWrapper
import com.alican.domain.mappers.toUIModel
import com.alican.domain.models.BaseUIModel
import com.alican.domain.models.MovieCreditsUIModel
import com.alican.domain.models.MovieDetailUIModel
import com.alican.domain.models.MovieReviewsUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieDetailInteractor @Inject constructor(
    private val repository: MoviesRepository
) {

    fun getMovieDetails(id: Int): Flow<BaseUIModel<MovieDetailUIModel>> {
        return flow {
            emit(BaseUIModel.Loading)
            emit(
                when (val response = repository.getMovieDetails(id)) {
                    is ResultWrapper.GenericError -> {
                        BaseUIModel.Error(response.error ?: "Error")
                    }

                    ResultWrapper.Loading -> {
                        BaseUIModel.Loading
                    }

                    ResultWrapper.NetworkError -> {
                        BaseUIModel.Error("Network Error")
                    }

                    is ResultWrapper.Success -> {
                        val uiModel = response.value.toUIModel()

                        BaseUIModel.Success(uiModel)
                    }
                }
            )
        }
    }

    fun getMovieImages(id: Int): Flow<BaseUIModel<List<String>>> {
        return flow {
            emit(BaseUIModel.Loading)
            emit(when (val response = repository.getMovieImages(id)) {
                is ResultWrapper.GenericError -> {
                    BaseUIModel.Error(response.error ?: "Error")
                }

                ResultWrapper.Loading -> {
                    BaseUIModel.Loading
                }

                ResultWrapper.NetworkError -> {
                    BaseUIModel.Error("Network Error")
                }

                is ResultWrapper.Success -> {
                    val uiModel =
                        response.value.posters?.filter { it.iso_639_1 == "en" }?.take(10)
                            ?.map { BuildConfig.BASE_POSTER_URL + it.file_path.orEmpty() }
                            ?: emptyList()

                    BaseUIModel.Success(uiModel)
                }
            })
        }
    }

    fun getMovieCredits(id: Int): Flow<BaseUIModel<List<MovieCreditsUIModel>>> {
        return flow {
            emit(BaseUIModel.Loading)
            emit(when (val response = repository.getMovieCredits(id)) {
                is ResultWrapper.GenericError -> {
                    BaseUIModel.Error(response.error ?: "Error")
                }

                ResultWrapper.Loading -> {
                    BaseUIModel.Loading
                }

                ResultWrapper.NetworkError -> {
                    BaseUIModel.Error("Network Error")
                }

                is ResultWrapper.Success -> {
                    val uiModel = response.value.cast?.map { it.toUIModel() } ?: emptyList()

                    BaseUIModel.Success(uiModel)
                }
            })
        }
    }

    fun getMovieReviews(id: Int, page: Int): Flow<BaseUIModel<List<MovieReviewsUIModel>>> {
        return flow {
            emit(BaseUIModel.Loading)
            emit(when (val response = repository.getMovieReviews(id, page)) {
                is ResultWrapper.GenericError -> {
                    BaseUIModel.Error(response.error ?: "Error")
                }

                ResultWrapper.Loading -> {
                    BaseUIModel.Loading
                }

                ResultWrapper.NetworkError -> {
                    BaseUIModel.Error("Network Error")
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