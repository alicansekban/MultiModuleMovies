package com.alican.data.utils

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val message: String? = null, val code: Int? = null) :
        ResultWrapper<Nothing>()
}
