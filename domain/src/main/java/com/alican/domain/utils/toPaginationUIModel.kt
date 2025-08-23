// Create this file: domain/src/main/java/com/alican/domain/utils/PaginationExtensions.kt
package com.alican.domain.utils

import com.alican.domain.models.BaseUIModel
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
    result: BaseUIModel<R>,
    page: Int,
    isFirstPage: Boolean = false,
    dataExtractor: (R) -> List<T>,
    totalPagesExtractor: (R) -> Int = { 1 },
    totalResultsExtractor: (R) -> Int = { 0 }
) {
    when (result) {
        is BaseUIModel.Loading -> {
            setLoading(isFirstPage)
        }

        is BaseUIModel.Success -> {
            val items = dataExtractor(result.data)
            val totalPages = totalPagesExtractor(result.data)
            val totalResults = totalResultsExtractor(result.data)

            setSuccess(
                newItems = items,
                currentPage = page,
                totalPages = totalPages,
                totalResults = totalResults,
                isFirstPage = isFirstPage
            )
        }

        is BaseUIModel.Error -> {
            setError(result.message, isFirstPage)
        }

        BaseUIModel.Empty -> {}
    }
}