package com.udacity.asteroidradar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.data.Constants
import com.udacity.asteroidradar.data.local.entities.AsteroidEntity

@Database(
    entities = [AsteroidEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    // singleton pattern
    companion object {
        private lateinit var INSTANCE: AsteroidDatabase
        fun getInstance(context: Context): AsteroidDatabase {
            synchronized(this){
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        Constants.DATABASE_NAME
                    ).build()
                }
            }
            return INSTANCE
        }
    }

}