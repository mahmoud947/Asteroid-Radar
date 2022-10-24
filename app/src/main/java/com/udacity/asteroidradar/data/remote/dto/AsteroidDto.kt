package com.udacity.asteroidradar.data.remote.dto

import com.squareup.moshi.Json

//data class AsteroidNewWsResponse(
//    val
//)


data class AsteroidDto(
    val id:String,
    @Json(name = "absolute_magnitude_h")
    val absoluteMagnitude:Double,
    @Json(name = "estimated_diameter")
    val estimatedDiameterDto: EstimatedDiameterDto,
    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous:Boolean,
    @Json(name = "close_approach_data")
    val closeApproachDataDto: List<CloseApproachDataDto>

)

data class EstimatedDiameterDto(
val kilometers:KilometersDto
)

data class KilometersDto(
    @Json(name = "estimated_diameter_max")
    val estimatedDiameterMax:Double
)

data class CloseApproachDataDto(
    @Json(name = "relative_velocity")
    val relativeVelocity:RelativeVelocityDto,
    @Json(name = "miss_distance")
    val miss_distance:MissDistanceDto
)

data class RelativeVelocityDto(
    @Json(name = "kilometers_per_second")
    val kilometersPerSecond:String
)

data class MissDistanceDto(
    @Json(name = "astronomical")
    val astronomical:String
)