package com.alican.multimodulemovies.components.widget

import com.alican.domain.models.MovieUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MovieWidgetComponentModel(
    val title: String? = null,
    val type: MovieType? = null,
    val items: ImmutableList<WidgetMovieModel> = persistentListOf(),
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

enum class MovieType{
    UPCOMING,
    NOW_PLAYING
}
