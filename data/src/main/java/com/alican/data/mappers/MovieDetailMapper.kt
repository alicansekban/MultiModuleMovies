package com.alican.data.mappers

import com.alican.data.data.response.CastItem
import com.alican.data.data.response.CrewItem
import com.alican.data.data.response.MovieCreditResponse
import com.alican.data.data.response.MovieDetailResponse
import com.alican.data.data.response.MovieReviewResponse
import com.alican.data.data.response.MovieReviewResponseItem
import com.alican.domain.models.Cast
import com.alican.domain.models.Crew
import com.alican.domain.models.MovieCredit
import com.alican.domain.models.MovieDetail
import com.alican.domain.models.MovieReview
import com.alican.domain.models.MovieReviews

fun MovieDetailResponse.toDomainModel(): MovieDetail {
    return MovieDetail(
        id = id ?: 0,
        title = title ?: "",
        overview = overview ?: "",
        posterPath = poster_path ?: "",
        backdropPath = backdrop_path ?: "",
        releaseDate = release_date ?: "",
        voteAverage = vote_average ?: 0.0,
        voteCount = voteCount ?: 0,
        runtime = runtime ?: 0
    )
}

fun MovieCreditResponse.toDomainModel(): MovieCredit {
    return MovieCredit(
        cast = cast?.map { it.toDomainModel() } ?: emptyList(),
        crew = crew?.mapNotNull { it?.toDomainModel() } ?: emptyList()
    )
}

fun CastItem.toDomainModel(): Cast {
    return Cast(
        id = id ?: 0,
        name = name ?: "",
        character = character ?: "",
        profilePath = profile_path ?: ""
    )
}

fun CrewItem.toDomainModel(): Crew {
    return Crew(
        id = id ?: 0,
        name = name ?: "",
        job = job ?: ""
    )
}

fun MovieReviewResponse.toDomainModel(): MovieReviews {
    return MovieReviews(
        page = page ?: 0,
        results = results?.map { it.toDomainModel() } ?: emptyList(),
        totalPages = total_pages ?: 0,
        totalResults = total_results ?: 0
    )
}

fun MovieReviewResponseItem.toDomainModel(): MovieReview {
    return MovieReview(
        author = author ?: "",
        content = content ?: ""
    )
}
