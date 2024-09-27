package com.alican.data.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseMoviesResponse(
    @SerialName("page")
    val page: Int? = null,
    @SerialName("results")
    val results: List<MovieResponse>? = emptyList(),
    @SerialName("total_pages")
    val total_pages: Int? = null,
    @SerialName("total_results")
    val total_results: Int? = null
)