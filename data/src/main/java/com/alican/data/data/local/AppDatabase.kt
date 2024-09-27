package com.alican.data.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alican.data.data.local.entity.MoviesEntity

@Database(
    entities = [MoviesEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
 }
