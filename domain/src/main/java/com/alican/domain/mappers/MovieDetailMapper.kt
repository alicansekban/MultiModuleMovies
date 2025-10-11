package com.alican.domain.mappers


import com.alican.domain.BuildConfig
import com.alican.domain.models.Cast
import com.alican.domain.models.MovieDetail
import com.alican.domain.models.MovieReview
import com.alican.domain.ui_models.movie.MovieCreditsUIModel
import com.alican.domain.ui_models.movie.MovieDetailUIModel
import com.alican.domain.ui_models.movie.MovieReviewsUIModel

fun MovieDetail.toUIModel(): MovieDetailUIModel {
    return MovieDetailUIModel(
        id = id,
        title = title,
        imageUrl = BuildConfig.BASE_POSTER_URL + posterPath,
        overview = overview,
        duration = "${this.runtime} min.",
        voteAvg = voteAverage.toString(),
        releaseDate = releaseDate
    )
}

fun Cast.toUIModel(): MovieCreditsUIModel {
    return MovieCreditsUIModel(
        id = id,
        characterName = character,
        imageUrl = BuildConfig.BASE_POSTER_URL + this.profilePath,
        name = name
    )
}

fun MovieReview.toUIModel(): MovieReviewsUIModel {
    return MovieReviewsUIModel(
        author = author,
        content = content
    )
}