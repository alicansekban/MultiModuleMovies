package com.alican.domain.models

data class MovieDetailUIModel(
    val id : Int? = 0,
    val title: String ? = null,
    val imageUrl: String? = null,
    val overview: String ?= null,
    val duration: String ?= null,
    val voteAvg: String ?= null,
    val releaseDate: String ?= null,
)
