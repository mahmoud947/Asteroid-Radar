package com.udacity.asteroidradar.domain.model

import com.squareup.moshi.Json

data class PictureOfDay(
    val mediaType: String,
    val title: String,
    val url: String
)