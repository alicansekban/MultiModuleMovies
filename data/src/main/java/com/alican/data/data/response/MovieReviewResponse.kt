package com.alican.data.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieReviewResponse(
    @SerialName("page")
    val page: Int? = null,
    @SerialName("results")
    val results: List<MovieReviewResponseItem>? = emptyList(),
    @SerialName("total_pages")
    val total_pages: Int? = null,
    @SerialName("total_results")
    val total_results: Int? = null
)

@Serializable
data class MovieReviewResponseItem(
    @SerialName("author_details")
    val author_details: AuthorDetails? = null,

    @SerialName("updated_at")
    val updated_at: String? = null,

    @SerialName("author")
    val author: String? = null,

    @SerialName("created_at")
    val created_at: String? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("content")
    val content: String? = null,

    @SerialName("url")
    val url: String? = null
)

@Serializable
data class AuthorDetails(

    @SerialName("avatar_path")
    val avatar_path: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("rating")
    val rating: Double? = null,

    @SerialName("username")
    val username: String? = null
)