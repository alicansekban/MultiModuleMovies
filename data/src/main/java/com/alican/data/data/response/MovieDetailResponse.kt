package com.alican.data.data.response

import kotlinx.serialization.SerialName


data class MovieDetailResponse(

    @SerialName("original_language")
	val original_language: String? = null,

    @SerialName("imdb_id")
	val imdb_id: String? = null,

    @SerialName("video")
	val video: Boolean? = null,

    @SerialName("title")
	val title: String? = null,

    @SerialName("backdrop_path")
	val backdrop_path: String? = null,

    @SerialName("revenue")
	val revenue: Int? = null,

    @SerialName("genres")
	val genres: List<GenresItem?>? = null,

    @SerialName("popularity")
	val popularity: Double? = null,

    @SerialName("production_countries")
	val productionCountries: List<ProductionCountriesItem?>? = null,

    @SerialName("id")
	val id: Int? = null,

    @SerialName("vote_count")
	val voteCount: Int? = null,

    @SerialName("budget")
	val budget: Int? = null,

    @SerialName("overview")
	val overview: String? = null,

    @SerialName("original_title")
	val originalTitle: String? = null,

    @SerialName("runtime")
	val runtime: Int? = null,

    @SerialName("poster_path")
	val poster_path: String? = null,

    @SerialName("spoken_languages")
	val spoken_languages: List<SpokenLanguagesItem?>? = null,

    @SerialName("production_companies")
	val production_companies: List<ProductionCompaniesItem?>? = null,

    @SerialName("release_date")
	val release_date: String? = null,

    @SerialName("vote_average")
	val vote_average: Double? = null,

    @SerialName("belongs_to_collection")
	val belongs_to_collection: BelongsToCollection? = null,

    @SerialName("tagline")
	val tagline: String? = null,

    @SerialName("adult")
	val adult: Boolean? = null,

    @SerialName("homepage")
	val homepage: String? = null,

    @SerialName("status")
	val status: String? = null
)

data class SpokenLanguagesItem(

	@SerialName("name")
	val name: String? = null,

	@SerialName("iso_639_1")
	val iso_639_1: String? = null,

	@SerialName("english_name")
	val englishName: String? = null
)

data class BelongsToCollection(

	@SerialName("backdrop_path")
	val backdrop_path: String? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: Int? = null,

	@SerialName("poster_path")
	val poster_path: String? = null
)

data class GenresItem(

	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: Int? = null
)

data class ProductionCountriesItem(

	@SerialName("iso_3166_1")
	val iso_3166_1: String? = null,

	@SerialName("name")
	val name: String? = null
)

data class ProductionCompaniesItem(

	@SerialName("logo_path")
	val logo_path: String? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: Int? = null,

	@SerialName("origin_country")
	val origin_country: String? = null
)
