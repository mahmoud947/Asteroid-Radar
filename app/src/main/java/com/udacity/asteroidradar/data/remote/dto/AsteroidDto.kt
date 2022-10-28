package com.udacity.asteroidradar.data.remote.dto

import com.squareup.moshi.Json
import com.udacity.asteroidradar.data.local.entities.AsteroidEntity

//data class AsteroidNewWsResponse(
//    val
//)


data class AsteroidDto(
    val id: String,
    val name: String,
    @Json(name = "absolute_magnitude_h")
    val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter")
    val estimatedDiameterDto: EstimatedDiameterDto,
    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean,
    @Json(name = "close_approach_data")
    val closeApproachDataDto: List<CloseApproachDataDto>

)

data class EstimatedDiameterDto(
    val kilometers: KilometersDto
)

data class KilometersDto(
    @Json(name = "estimated_diameter_max")
    val estimatedDiameterMax: Double
)

data class CloseApproachDataDto(
    @Json(name = "close_approach_date")
    val closeApproachDate: String,
    @Json(name = "relative_velocity")
    val relativeVelocity: RelativeVelocityDto,
    @Json(name = "miss_distance")
    val missDistance: MissDistanceDto
)

data class RelativeVelocityDto(
    @Json(name = "kilometers_per_second")
    val kilometersPerSecond: String
)

data class MissDistanceDto(
    @Json(name = "astronomical")
    val astronomical: String
)

/**
 * mapping functions
 * convert AsteroidDto to AsteroidEntity that will use with room database
 */
fun AsteroidDto.toEntity(): AsteroidEntity =
    AsteroidEntity(
        id = this.id.toLong(),
        codename = this.name,
        absoluteMagnitude = this.absoluteMagnitude,
        closeApproachDate = this.closeApproachDataDto[0].closeApproachDate,
        distanceFromEarth = this.closeApproachDataDto[0].missDistance.astronomical.toDouble(),
        relativeVelocity = this.closeApproachDataDto[0].relativeVelocity.kilometersPerSecond.toDouble(),
        estimatedDiameter = this.estimatedDiameterDto.kilometers.estimatedDiameterMax,
        isPotentiallyHazardous = this.isPotentiallyHazardous
    )

/**
 * mapping functions
 * convert List of AsteroidDto to List of AsteroidEntity that will use with room database
 */

fun List<AsteroidDto>.toEntity(): Array<AsteroidEntity> =
    map {
        it.toEntity()
    }.toTypedArray()


















