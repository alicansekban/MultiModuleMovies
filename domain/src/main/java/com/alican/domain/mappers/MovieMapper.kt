package com.alican.domain.mappers

import com.alican.data.BuildConfig
import com.alican.data.data.response.BaseMoviesResponse
import com.alican.data.data.response.MovieResponse
import com.alican.domain.models.MovieListUIModel
import com.alican.domain.models.MovieUIModel
import kotlinx.collections.immutable.adapters.ImmutableListAdapter

fun MovieResponse.toUIModel() : MovieUIModel {
    return MovieUIModel(
        id = id,
        title = title,
        imageUrl = BuildConfig.BASE_POSTER_URL + this.poster_path
    )
}

fun BaseMoviesResponse.toUIModel(currentModel : MovieListUIModel) : MovieListUIModel {
    val newMovies = this.results?.map { it.toUIModel() } ?: emptyList()
    val updatedMovies = currentModel.movies.plus(newMovies).distinctBy { it.id }
    return MovieListUIModel(
        page = page ?: 0,
        movies = ImmutableListAdapter(impl = updatedMovies),
        totalPages = total_pages ?: 0,
        totalResults = total_results ?: 0,
        canLoadMore = page != total_pages

    )
}