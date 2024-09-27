package com.alican.data.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MoviesEntity(
    @PrimaryKey
    val cityName : String,
    val cityId : String
)
