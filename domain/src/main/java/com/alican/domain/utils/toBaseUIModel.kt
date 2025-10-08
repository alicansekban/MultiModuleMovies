package com.alican.domain.utils

import com.alican.data.utils.ResultWrapper
import com.alican.domain.ui_models.BaseUIModel

fun <T> ResultWrapper<T>.toBaseUIModel(): BaseUIModel<T> {
    return when (this) {
        is ResultWrapper.Success -> {
            if (value == null) {
                BaseUIModel.Empty
            } else {
                BaseUIModel.Success(value)
            }
        }

        is ResultWrapper.Error -> BaseUIModel.Error(message ?: "Unknown error")
        is ResultWrapper.Loading -> BaseUIModel.Loading
    }
}

// Extension for handling list responses that might be empty
fun <T> ResultWrapper<T>.toBaseUIModelWithEmptyCheck(
    isEmpty: (T) -> Boolean
): BaseUIModel<T> {
    return when (this) {
        is ResultWrapper.Success -> {
            when {
                value == null -> BaseUIModel.Empty
                isEmpty(value) -> BaseUIModel.Empty
                else -> BaseUIModel.Success(value)
            }
        }

        is ResultWrapper.Error -> BaseUIModel.Error(message ?: "Unknown error")
        is ResultWrapper.Loading -> BaseUIModel.Loading
    }
}