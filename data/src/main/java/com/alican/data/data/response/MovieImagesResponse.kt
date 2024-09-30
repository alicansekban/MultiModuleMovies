package com.alican.data.data.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MovieImagesResponse(

	@SerialName("backdrops")
	val backdrops: List<BackdropsItem>? = null,

	@SerialName("posters")
	val posters: List<PostersItem>? = null,

	@SerialName("id")
	val id: Int? = null,

	@SerialName("logos")
	val logos: List<LogosItem>? = null
)

@Serializable
data class PostersItem(

	@SerialName("aspect_ratio")
	val aspect_ratio: Double? = null,

	@SerialName("file_path")
	val file_path: String? = null,

	@SerialName("vote_average")
	val vote_average: Double? = null,

	@SerialName("width")
	val width: Int? = null,

	@SerialName("iso_639_1")
	val iso_639_1: String? = null,

	@SerialName("vote_count")
	val vote_count: Int? = null,

	@SerialName("height")
	val height: Int? = null
)

@Serializable
data class LogosItem(

	@SerialName("aspect_ratio")
	val aspect_ratio: Double? = null,

	@SerialName("file_path")
	val file_path: String? = null,

	@SerialName("vote_average")
	val vote_average: Double? = null,

	@SerialName("width")
	val width: Int? = null,

	@SerialName("iso_639_1")
	val iso_639_1: String? = null,

	@SerialName("vote_count")
	val vote_count: Int? = null,

	@SerialName("height")
	val height: Int? = null
)

@Serializable
data class BackdropsItem(

	@SerialName("aspect_ratio")
	val aspect_ratio: Double? = null,

	@SerialName("file_path")
	val file_path: String? = null,

	@SerialName("vote_average")
	val vote_average: Double? = null,

	@SerialName("width")
	val width: Int? = null,

	@SerialName("iso_639_1")
	val iso_639_1: String? = null,

	@SerialName("vote_count")
	val vote_count: Int? = null,

	@SerialName("height")
	val height: Int? = null
)
