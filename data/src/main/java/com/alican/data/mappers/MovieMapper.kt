package com.alican.data.mappers

import com.alican.data.data.response.BaseMoviesResponse
import com.alican.data.data.response.MovieResponse
import com.alican.domain.models.Movie
import com.alican.domain.models.MovieList

fun MovieResponse.toDomainModel(): Movie {
    return Movie(
        id = id ?: 0,
        title = title ?: "",
        posterPath = poster_path ?: "",
        overview = overview ?: ""
    )
}

fun BaseMoviesResponse.toDomainModel(): MovieList {
    return MovieList(
        page = page ?: 0,
        results = results?.map { it.toDomainModel() } ?: emptyList(),
        totalPages = total_pages ?: 0,
        totalResults = total_results ?: 0
    )
}
