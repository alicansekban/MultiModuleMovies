package com.alican.domain.models

sealed class BaseUIModel<out T> {
    data class Success<T>(val data: T) : BaseUIModel<T>()
    data class Error<T>(val message: String) : BaseUIModel<T>()
    data object Loading : BaseUIModel<Nothing>()
    data object Empty : BaseUIModel<Nothing>()

}