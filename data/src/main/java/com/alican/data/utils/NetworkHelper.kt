package com.alican.data.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException

@Deprecated(
    message = "Use safeCall(HttpClient, HttpRequestBuilder) instead.",
    replaceWith = ReplaceWith("safeCall(client, requestBuilder)")
)
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                else -> ResultWrapper.GenericError()
            }
        }
    }
}

suspend inline fun <reified T> safeCall(
    client: HttpClient,
    requestBuilder: HttpRequestBuilder.() -> Unit
): ResultWrapper<T> {
    return try {
        val response: HttpResponse = client.request {
            requestBuilder()
        }
        if (response.status.isSuccess()) {
            val responseData: T = response.body()
            ResultWrapper.Success(responseData)
        } else {
            ResultWrapper.GenericError(response.status.value)
        }
    } catch (throwable: Throwable) {
        when (throwable) {
            is ConnectTimeoutException,
            is IOException -> ResultWrapper.NetworkError

            else -> ResultWrapper.GenericError()
        }
    }
}