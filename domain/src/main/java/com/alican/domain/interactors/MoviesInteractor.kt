package com.alican.domain.interactors

import com.alican.data.data.repository.MoviesRepository
import com.alican.data.utils.ResultWrapper
import com.alican.domain.mappers.toUIModel
import com.alican.domain.models.BaseUIModel
import com.alican.domain.models.MovieUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesInteractor @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend fun getUpComingMovies(page: Int): Flow<BaseUIModel<List<MovieUIModel>>> {
        return flow {
            emit(BaseUIModel.Loading)
            emit(when (val response = moviesRepository.getUpComingMovies(page)) {
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