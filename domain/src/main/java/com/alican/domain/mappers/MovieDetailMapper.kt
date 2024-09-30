package com.alican.domain.mappers

import com.alican.data.BuildConfig
import com.alican.data.data.response.MovieDetailResponse
import com.alican.domain.models.MovieDetailUIModel

fun MovieDetailResponse.toUIModel(): MovieDetailUIModel {
    return MovieDetailUIModel(
        id = id,
        title = title,
        imageUrl = BuildConfig.BASE_POSTER_URL + this.poster_path,
        overview = overview,
        duration = "${this.runtime} min.",
        voteAvg = vote_average.toString(),
        releaseDate = release_date
        )
}