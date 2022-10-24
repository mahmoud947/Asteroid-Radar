package com.udacity.asteroidradar.data.remote

import com.udacity.asteroidradar.data.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()


interface AsteroidsNeoWsService {
    @GET("neo/rest/v1/feed?api_key=${Constants.API_KEY}")
    suspend fun getAsteroids(): String
}

object AsteroidNewWsApi {
    fun getInstance(): AsteroidsNeoWsService {
        val asteroidsNeoWsService: AsteroidsNeoWsService by lazy {
            retrofit.create(AsteroidsNeoWsService::class.java)
        }
        return asteroidsNeoWsService
    }
}

