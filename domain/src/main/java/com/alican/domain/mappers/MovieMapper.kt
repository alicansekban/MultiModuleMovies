package com.alican.domain.mappers

import com.alican.data.BuildConfig
import com.alican.data.data.response.MovieResponse
import com.alican.domain.models.MovieUIModel

fun MovieResponse.toUIModel() : MovieUIModel {
    return MovieUIModel(
        id = id,
        title = title,
        imageUrl = BuildConfig.BASE_POSTER_URL + this.poster_path
    )
}