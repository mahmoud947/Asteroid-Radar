package com.udacity.asteroidradar.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.local.entities.AsteroidEntity
import com.udacity.asteroidradar.data.local.entities.PictureOfDayEntity

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroidEntity: AsteroidEntity)

    @Query("SELECT * FROM asteroid_entity ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_entity WHERE closeApproachDate=:day ORDER BY closeApproachDate ASC")
    fun getTodayAsteroids(day: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_entity WHERE closeApproachDate BETWEEN :start AND :end ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(start: String, end: String): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroid_entity")
    suspend fun clear()

}