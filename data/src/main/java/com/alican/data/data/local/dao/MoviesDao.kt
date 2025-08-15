package com.alican.data.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.alican.data.data.local.entity.MoviesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Query("SELECT * FROM favorite_movies ORDER BY addedAt DESC")
    fun getFavoriteMovies(): Flow<List<MoviesEntity>>

    @Query("SELECT movieId FROM favorite_movies")
    fun getFavoriteMovieIds(): Flow<List<Int>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE movieId = :movieId)")
    fun isMovieFavorite(movieId: Int): Flow<Boolean>

    @Insert(onConflict = REPLACE)
    suspend fun addToFavorites(movie: MoviesEntity)

    @Query("DELETE FROM favorite_movies WHERE movieId = :movieId")
    suspend fun removeFromFavorites(movieId: Int)

    @Query("SELECT COUNT(*) FROM favorite_movies")
    fun getFavoritesCount(): Flow<Int>
}