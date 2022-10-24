package com.udacity.asteroidradar.data.remote

import com.udacity.asteroidradar.data.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

/**
 * set connection and read time out
 * to avoid slow API response time
 */
private val clint:OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(Constants.CONNECTION_TIMEOUT,TimeUnit.SECONDS)
    .readTimeout(Constants.READ_TIMEOUT,TimeUnit.SECONDS)
    .build()

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .client(clint)
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

