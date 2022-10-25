package com.udacity.asteroidradar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroid_entity")
data class AsteroidEntity(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)
