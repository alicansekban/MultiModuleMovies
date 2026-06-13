package com.alican.data.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

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
            ResultWrapper.Error(message = throwable.message)
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
            ResultWrapper.Error(message = response.status.description, code = response.status.value)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (throwable: Throwable) {
        ResultWrapper.Error(message = throwable.message)
    }
}