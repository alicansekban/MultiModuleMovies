package com.alican.domain.mappers

import com.alican.domain.BuildConfig
import com.alican.domain.models.Movie
import com.alican.domain.models.MovieList
import com.alican.domain.ui_models.movie.MovieListUIModel
import com.alican.domain.ui_models.movie.MovieUIModel

fun Movie.toUIModel(): MovieUIModel {
    return MovieUIModel(
        id = id,
        title = title,
        imageUrl = BuildConfig.BASE_POSTER_URL + this.posterPath
    )
}

fun MovieList.toUIModel(currentModel: MovieListUIModel): MovieListUIModel {
    val newMovies = this.results.map { it.toUIModel() }
    val updatedMovies = currentModel.movies.plus(newMovies).distinctBy { it.id }
    return MovieListUIModel(
        page = page,
        movies = updatedMovies,
        totalPages = totalPages,
        totalResults = totalResults,
        canLoadMore = page != totalPages

    )
}