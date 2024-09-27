package com.alican.data.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> {
                    ResultWrapper.NetworkError
                }

                is HttpException -> {
                    val code = throwable.code()
                    val errorMessage = throwable.message()
                    ResultWrapper.GenericError(code, errorMessage)
                }

                else -> {
                    ResultWrapper.GenericError()
                }
            }
        }
    }
}
