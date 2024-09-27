package com.alican.data.utils

sealed class ResultWrapper<out T> {
    data object Loading : ResultWrapper<Nothing>()
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null) :
        ResultWrapper<Nothing>()

    data object NetworkError : ResultWrapper<Nothing>()
}
