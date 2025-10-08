package com.alican.multimodulemovies.components.widget

import com.alican.domain.ui_models.movie.MovieType
import com.alican.domain.ui_models.movie.MovieUIModel

data class MovieWidgetComponentModel(
    val title: String? = null,
    val type: MovieType? = null,
    val items: List<WidgetMovieModel> = emptyList(),
)

data class WidgetMovieModel(
    val id: Int? = null,
    val imageUrl: String? = null,
    val title: String? = null,
)

fun MovieUIModel.toWidgetModel() : WidgetMovieModel{
    return WidgetMovieModel(
        id = id,
        imageUrl = imageUrl,
        title = title,
    )
}