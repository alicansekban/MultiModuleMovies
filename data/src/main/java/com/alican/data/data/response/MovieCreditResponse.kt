package com.alican.data.data.response

import kotlinx.serialization.SerialName


data class MovieCreditResponse(

    @SerialName("cast")
	val cast: List<CastItem?>? = null,

    @SerialName("id")
	val id: Int? = null,

    @SerialName("crew")
	val crew: List<CrewItem?>? = null
)

data class CastItem(

	@SerialName("cast_id")
	val cast_id: Int? = null,

	@SerialName("character")
	val character: String? = null,

	@SerialName("gender")
	val gender: Int? = null,

	@SerialName("credit_id")
	val credit_id: String? = null,

	@SerialName("known_for_department")
	val known_for_department: String? = null,

	@SerialName("original_name")
	val original_name: String? = null,

	@SerialName("popularity")
	val popularity: Double? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("profile_path")
	val profile_path: String? = null,

	@SerialName("id")
	val id: Int? = null,

	@SerialName("adult")
	val adult: Boolean? = null,

	@SerialName("order")
	val order: Int? = null
)

data class CrewItem(

	@SerialName("gender")
	val gender: Int? = null,

	@SerialName("credit_id")
	val credit_id: String? = null,

	@SerialName("known_for_department")
	val known_for_department: String? = null,

	@SerialName("original_name")
	val original_name: String? = null,

	@SerialName("popularity")
	val popularity: Any? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("profile_path")
	val profile_path: String? = null,

	@SerialName("id")
	val id: Int? = null,

	@SerialName("adult")
	val adult: Boolean? = null,

	@SerialName("department")
	val department: String? = null,

	@SerialName("job")
	val job: String? = null
)
