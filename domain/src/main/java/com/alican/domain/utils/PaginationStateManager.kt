// Create this file: domain/src/main/java/com/alican/domain/utils/PaginationStateManager.kt
package com.alican.domain.utils

import com.alican.domain.models.pagination.PaginationUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaginationStateManager<T> {

    private val _state = MutableStateFlow(PaginationUIModel<T>())
    val state: StateFlow<PaginationUIModel<T>> = _state.asStateFlow()

    private var currentItems = mutableListOf<T>()

    fun setLoading(isFirstPage: Boolean = true) {
        _state.value = _state.value.copy(
            isLoading = isFirstPage,
            isLoadingMore = !isFirstPage,
            hasError = false,
            errorMessage = null
        )
    }

    fun setSuccess(
        newItems: List<T>,
        currentPage: Int,
        totalPages: Int,
        totalResults: Int,
        isFirstPage: Boolean = false
    ) {
        if (isFirstPage) {
            currentItems.clear()
        }

        currentItems.addAll(newItems)

        _state.value = PaginationUIModel(
            items = currentItems.toList(),
            currentPage = currentPage,
            totalPages = totalPages,
            totalResults = totalResults,
            isLoading = false,
            isLoadingMore = false,
            hasError = false,
            errorMessage = null,
            canLoadMore = currentPage < totalPages
        )
    }

    fun setError(errorMessage: String, isFirstPage: Boolean = true) {
        _state.value = _state.value.copy(
            isLoading = false,
            isLoadingMore = false,
            hasError = true,
            errorMessage = errorMessage,
            canLoadMore = if (isFirstPage) false else _state.value.canLoadMore
        )
    }

    fun reset() {
        currentItems.clear()
        _state.value = PaginationUIModel()
    }

    fun retry() {
        if (_state.value.hasError) {
            val isFirstPage = _state.value.items.isEmpty()
            setLoading(isFirstPage)
        }
    }
}