package com.alican.data.data.response

import kotlinx.serialization.SerialName

data class ErrorResponse(
    @SerialName("errors")
    val errors: List<ErrorsItem?>? = null,

    @SerialName("message")
    val message: String? = null,

    @SerialName( "code")
    val code: String? = null
)

data class ErrorsItem(

	@SerialName("detail")
	val detail: String? = null,

	@SerialName("status")
	val status: String? = null
)
