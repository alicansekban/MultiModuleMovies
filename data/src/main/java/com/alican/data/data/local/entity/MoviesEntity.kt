package com.alican.data.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class MoviesEntity(
    @PrimaryKey
    val movieId: Int,
    val title: String,
    val imageUrl: String?,
    val overview: String?,
    val addedAt: Long = System.currentTimeMillis()
)
