package com.udacity.asteroidradar.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.data.Constants
import com.udacity.asteroidradar.data.remote.dto.PictureOfDayDto
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * set connection and read time out
 * to avoid slow API response time
 */
private val clint: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
    .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
    .build()

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(clint)
    .build()


interface AsteroidsNeoWsService {
    @GET("neo/rest/v1/feed?api_key=${Constants.API_KEY}")
    suspend fun getAsteroids(): String

    @GET("/planetary/apod?api_key=${Constants.API_KEY}")
    suspend fun getPictureOfDayDto(): PictureOfDayDto
}

object AsteroidNewWsApi {
    fun getInstance(): AsteroidsNeoWsService {
        val asteroidsNeoWsService: AsteroidsNeoWsService by lazy {
            retrofit.create(AsteroidsNeoWsService::class.java)
        }
        return asteroidsNeoWsService
    }
}

