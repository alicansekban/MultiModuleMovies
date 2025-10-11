package com.alican.data.mappers

import com.alican.data.data.local.entity.MoviesEntity
import com.alican.domain.models.Movie

fun MoviesEntity.toDomainModel(): Movie {
    return Movie(
        id = movieId,
        title = title,
        posterPath = imageUrl ?: "",
        overview = overview ?: ""
    )
}

fun Movie.toEntity(): MoviesEntity {
    return MoviesEntity(
        movieId = id,
        title = title,
        imageUrl = posterPath,
        overview = overview
    )
}
