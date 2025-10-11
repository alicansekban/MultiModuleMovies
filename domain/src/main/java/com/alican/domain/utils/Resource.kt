package com.alican.domain.utils

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Error(val message: String? = null, val code: Int? = null) : Resource<Nothing>()
}
