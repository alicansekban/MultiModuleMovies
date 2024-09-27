package com.alican.data.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.alican.data.data.local.entity.MoviesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertCities(movies: List<MoviesEntity>)

    @Query("SELECT * FROM movies")
    suspend fun getMovies() : Flow<List<MoviesEntity>>

}
