package com.alican.data.data.local.response

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

data class ErrorResponse(
    @Json(name = "errors")
    val errors: List<ErrorsItem?>? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "code")
    val code: String? = null
)

data class ErrorsItem(

	@Json(name = "detail")
	val detail: String? = null,

	@Json(name = "status")
	val status: String? = null
)
