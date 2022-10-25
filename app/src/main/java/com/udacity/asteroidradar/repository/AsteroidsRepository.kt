package com.udacity.asteroidradar.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.local.AsteroidDao
import com.udacity.asteroidradar.data.local.entities.toDomain
import com.udacity.asteroidradar.data.remote.AsteroidsNeoWsService
import com.udacity.asteroidradar.data.remote.dto.AsteroidDto
import com.udacity.asteroidradar.data.remote.dto.toAsteroidEntity
import com.udacity.asteroidradar.data.util.ConnectionState
import com.udacity.asteroidradar.data.util.JsonParser
import com.udacity.asteroidradar.data.util.NetworkResult
import com.udacity.asteroidradar.domain.model.Asteroid
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class AsteroidsRepository(
    private val networkService: AsteroidsNeoWsService,
    private val asteroidDao: AsteroidDao,
    private val context: Context
) {
    val asteroids: LiveData<List<Asteroid>> = Transformations.map(asteroidDao.getAsteroids()) {
        it.toDomain()
    }
    private val _connectionState: MutableLiveData<ConnectionState> =
        MutableLiveData(ConnectionState.Connected())
    val connectionState: LiveData<ConnectionState> = _connectionState

    suspend fun refreshAsteroidDatabase() {
        when (val networkResult = fetchDataFromInternet()) {
            is NetworkResult.OnSuccess -> {
                val result = networkResult.data?.map {
                    it.toAsteroidEntity()
                }?.toTypedArray()
                asteroidDao.insertAll(* result!!)
            }
            is NetworkResult.OnError -> {
                _connectionState.value = ConnectionState.Disconnected(networkResult.errorMessage!!)
            }
        }
    }

    private suspend fun fetchDataFromInternet(): NetworkResult<List<AsteroidDto>> {
        return try {
            val response = networkService.getAsteroids()
            val jsonObject: JSONObject = JSONObject(response)
            val asteroidsDto =
                JsonParser.getInstance().parseJsonResultAsAsteroid(jsonObject = jsonObject)
            NetworkResult.OnSuccess(asteroidsDto)
        } catch (e: IOException) {
            NetworkResult.OnError(errorMessage = context.getString(R.string.un_known_error))
        } catch (e: HttpException) {
            NetworkResult.OnError(errorMessage = context.getString(R.string.check_your_internet_connection))
        }
    }

}