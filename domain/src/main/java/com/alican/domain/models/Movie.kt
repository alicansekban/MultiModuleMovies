package com.alican.domain.models

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val overview: String
)

data class MovieList(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val runtime: Int
)

data class MovieCredit(
    val cast: List<Cast>,
    val crew: List<Crew>
)

data class Cast(
    val id: Int,
    val name: String,
    val character: String,
    val profilePath: String?
)

data class Crew(
    val id: Int,
    val name: String,
    val job: String
)

data class MovieReview(
    val author: String,
    val content: String
)

data class MovieReviews(
    val page: Int,
    val results: List<MovieReview>,
    val totalPages: Int,
    val totalResults: Int
)

data class MovieImage(
    val filePath: String,
    val iso_639_1: String
)

data class MovieImages(
    val backdrops: List<MovieImage>,
    val posters: List<MovieImage>
)