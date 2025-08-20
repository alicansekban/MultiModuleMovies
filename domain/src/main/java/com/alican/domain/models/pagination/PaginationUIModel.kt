// Create this file: domain/src/main/java/com/alican/domain/models/PaginationUIModel.kt
package com.alican.domain.models.pagination

data class PaginationUIModel<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val totalResults: Int = 0,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String? = null,
    val canLoadMore: Boolean = false
) {
    val isEmpty: Boolean
        get() = items.isEmpty() && !isLoading

    val isFirstPage: Boolean
        get() = currentPage == 1

    val hasNextPage: Boolean
        get() = currentPage < totalPages

    val nextPage: Int
        get() = if (hasNextPage) currentPage + 1 else currentPage
}