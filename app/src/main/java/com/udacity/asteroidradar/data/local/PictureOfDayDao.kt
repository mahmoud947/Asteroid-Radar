package com.udacity.asteroidradar.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.local.entities.PictureOfDayEntity

@Dao
interface PictureOfDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(pictureOfDayEntity: PictureOfDayEntity)

    @Query("SELECT * FROM picture_of_day_entity")
    fun getPictureOfTheDay(): LiveData<PictureOfDayEntity>

    @Query("DELETE FROM picture_of_day_entity")
    suspend fun deletePictureOfTheDay()

}