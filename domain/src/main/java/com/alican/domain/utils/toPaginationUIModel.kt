// Create this file: domain/src/main/java/com/alican/domain/utils/PaginationExtensions.kt
package com.alican.domain.utils

import com.alican.data.utils.ResultWrapper
import com.alican.domain.models.MovieListUIModel
import com.alican.domain.models.MovieUIModel
import com.alican.domain.models.pagination.PaginationUIModel

// Extension to convert MovieListUIModel to PaginationUIModel
fun <T> MovieListUIModel.toPaginationUIModel(
    itemMapper: (List<MovieUIModel>) -> List<T>
): PaginationUIModel<T> {
    return PaginationUIModel(
        items = itemMapper(this.movies),
        currentPage = this.page,
        totalPages = this.totalPages,
        totalResults = this.totalResults,
        canLoadMore = this.canLoadMore
    )
}

// Extension to handle ResultWrapper and update PaginationStateManager
fun <T, R> PaginationStateManager<T>.handleResult(
    result: ResultWrapper<R>,
    page: Int,
    isFirstPage: Boolean = false,
    dataExtractor: (R) -> List<T>,
    totalPagesExtractor: (R) -> Int = { 1 },
    totalResultsExtractor: (R) -> Int = { 0 }
) {
    when (result) {
        is ResultWrapper.Loading -> {
            setLoading(isFirstPage)
        }

        is ResultWrapper.Success -> {
            val items = dataExtractor(result.value)
            val totalPages = totalPagesExtractor(result.value)
            val totalResults = totalResultsExtractor(result.value)

            setSuccess(
                newItems = items,
                currentPage = page,
                totalPages = totalPages,
                totalResults = totalResults,
                isFirstPage = isFirstPage
            )
        }

        is ResultWrapper.Error -> {
            setError(result.message ?: "Unknown error", isFirstPage)
        }
    }
}