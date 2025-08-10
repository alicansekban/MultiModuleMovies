package com.alican.data.utils

sealed class ResultWrapper<out T> {
    data object Loading : ResultWrapper<Nothing>()
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val message: String? = null) :
        ResultWrapper<Nothing>()
}
