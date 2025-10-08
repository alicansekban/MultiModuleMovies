// Create this file: domain/src/main/java/com/alican/domain/interactors/BasePaginatedInteractor.kt
package com.alican.domain.interactors

import com.alican.domain.ui_models.BaseUIModel
import com.alican.domain.ui_models.pagination.PaginationUIModel
import com.alican.domain.utils.PaginationStateManager
import com.alican.domain.utils.handleResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BasePaginatedInteractor<T, R> {

    protected val paginationManager = PaginationStateManager<T>()
    protected abstract val coroutineScope: CoroutineScope

    val paginationState: Flow<PaginationUIModel<T>> = paginationManager.state

    abstract suspend fun fetchData(page: Int): BaseUIModel<R>
    abstract fun mapToUIModel(data: R): List<T>
    abstract fun getTotalPages(data: R): Int
    abstract fun getTotalResults(data: R): Int

    suspend fun loadFirstPage() {
        paginationManager.handleResult(
            result = fetchData(1),
            page = 1,
            isFirstPage = true,
            dataExtractor = ::mapToUIModel,
            totalPagesExtractor = ::getTotalPages,
            totalResultsExtractor = ::getTotalResults
        )
    }

    suspend fun loadNextPage() {
        val currentState = paginationManager.state.value
        if (currentState.hasNextPage && !currentState.isLoadingMore) {
            paginationManager.handleResult(
                result = fetchData(currentState.nextPage),
                page = currentState.nextPage,
                isFirstPage = false,
                dataExtractor = ::mapToUIModel,
                totalPagesExtractor = ::getTotalPages,
                totalResultsExtractor = ::getTotalResults
            )
        }
    }

    fun retry() {
        coroutineScope.launch {
            val currentState = paginationManager.state.value
            val isFirstPage = currentState.items.isEmpty()

            if (isFirstPage) {
                loadFirstPage()
            } else {
                loadNextPage()
            }
        }
    }


    suspend fun reset() {
        paginationManager.reset()
    }
}