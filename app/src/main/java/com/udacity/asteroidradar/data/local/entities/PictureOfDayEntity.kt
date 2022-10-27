package com.udacity.asteroidradar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.model.PictureOfDay

@Entity(tableName = "picture_of_day_entity")
data class PictureOfDayEntity(
    val mediaType: String,
    val title: String,
    @PrimaryKey
    val url: String
)

/**
 * mapping functions
 * convert PictureOfDayDto to PictureOfDay in domain
 */
fun PictureOfDayEntity.toDomain(): PictureOfDay =
    PictureOfDay(mediaType = this.mediaType, title = this.title, url = this.url)

