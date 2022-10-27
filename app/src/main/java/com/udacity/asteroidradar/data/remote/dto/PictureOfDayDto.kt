package com.udacity.asteroidradar.data.remote.dto

import com.squareup.moshi.Json
import com.udacity.asteroidradar.data.local.entities.AsteroidEntity
import com.udacity.asteroidradar.data.local.entities.PictureOfDayEntity
import com.udacity.asteroidradar.domain.model.PictureOfDay

data class PictureOfDayDto(
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
)

/**
 * mapping functions
 * convert PictureOfDayDto to PictureOfDay in domain
 */
fun PictureOfDayDto.toEntity(): PictureOfDayEntity =
    PictureOfDayEntity(mediaType = this.mediaType, title = this.title, url = this.url)

