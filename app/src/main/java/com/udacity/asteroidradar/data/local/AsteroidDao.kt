package com.udacity.asteroidradar.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.local.entities.AsteroidEntity

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroidEntity: AsteroidEntity)

    @Query("SELECT * FROM asteroid_entity ORDER BY closeApproachDate DESC")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_entity WHERE closeApproachDate=:day ORDER BY closeApproachDate DESC")
    fun getTodayAsteroids(day: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_entity WHERE closeApproachDate BETWEEN :start AND :end ORDER BY closeApproachDate DESC")
    fun getWeekAsteroids(start: String, end: String): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroid_entity WHERE closeApproachDate <:day")
    suspend fun deleteAsteroidsBeforeToday(day: String)

}