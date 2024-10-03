package com.alican.domain.mappers

import com.alican.data.BuildConfig
import com.alican.data.data.response.CastItem
import com.alican.data.data.response.MovieDetailResponse
import com.alican.data.data.response.MovieReviewResponseItem
import com.alican.domain.models.MovieCreditsUIModel
import com.alican.domain.models.MovieDetailUIModel
import com.alican.domain.models.MovieReviewsUIModel

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

fun CastItem.toUIModel(): MovieCreditsUIModel {
    return MovieCreditsUIModel(
        id = id,
        characterName = character,
        imageUrl = BuildConfig.BASE_POSTER_URL + this.profile_path,
        name = name
    )
}

fun MovieReviewResponseItem.toUIModel(): MovieReviewsUIModel {
    return MovieReviewsUIModel(
        id = id,
        author = author,
        content = content,
        url = url
    )
}