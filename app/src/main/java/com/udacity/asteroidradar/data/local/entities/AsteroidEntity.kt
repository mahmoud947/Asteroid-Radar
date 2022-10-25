package com.udacity.asteroidradar.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.model.Asteroid

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

/**
 * mapping functions
 * convert list of AsteroidEntity to list of Asteroid(domain)
 */
fun List<AsteroidEntity>.toDomain(): List<Asteroid> =
  map {
      Asteroid(
          id = it.id,
          codename = it.codename,
          absoluteMagnitude = it.absoluteMagnitude,
          closeApproachDate = it.closeApproachDate,
          estimatedDiameter = it.estimatedDiameter,
          relativeVelocity = it.relativeVelocity,
          distanceFromEarth = it.distanceFromEarth,
          isPotentiallyHazardous = it.isPotentiallyHazardous
      )
  }