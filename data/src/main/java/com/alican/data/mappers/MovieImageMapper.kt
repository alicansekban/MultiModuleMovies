package com.alican.data.mappers

import com.alican.data.data.response.BackdropsItem
import com.alican.data.data.response.MovieImagesResponse
import com.alican.data.data.response.PostersItem
import com.alican.domain.models.MovieImage
import com.alican.domain.models.MovieImages

fun MovieImagesResponse.toDomainModel(): MovieImages {
    return MovieImages(
        backdrops = backdrops?.map { it.toDomainModel() } ?: emptyList(),
        posters = posters?.map { it.toDomainModel() } ?: emptyList()
    )
}

fun BackdropsItem.toDomainModel(): MovieImage {
    return MovieImage(
        filePath = file_path ?: "",
        iso_639_1 = iso_639_1.orEmpty()
    )
}

fun PostersItem.toDomainModel(): MovieImage {
    return MovieImage(
        filePath = file_path ?: "",
        iso_639_1 = iso_639_1.orEmpty()
    )
}
