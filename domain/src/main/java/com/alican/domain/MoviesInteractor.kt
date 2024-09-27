package com.alican.domain

import com.alican.data.data.repository.MoviesRepository
import com.alican.data.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesInteractor @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    suspend fun getMovies(page: Int): Flow<List<MovieUIModel>> {
        return flow {
            when (val response = moviesRepository.getUpComingMovies(page)) {
                is ResultWrapper.GenericError -> {}
                ResultWrapper.Loading -> {}
                ResultWrapper.NetworkError -> {}
                is ResultWrapper.Success -> {
                    emit(response.value.results?.map {
                        MovieUIModel(
                            id = it.id ?: 0,
                            title = it.title ?: "",
                            posterPath = it.poster_path ?: "",
                            overview = it.overview ?: ""
                        )
                    } ?: emptyList())
                }
            }
        }


    }
}