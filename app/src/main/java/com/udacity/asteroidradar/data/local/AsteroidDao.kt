package com.udacity.asteroidradar.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.local.entities.AsteroidEntity

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroidEntity: AsteroidEntity)

    @Query("SELECT * FROM asteroid_entity")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroid_entity")
    suspend fun clear()

}