package com.udacity.asteroidradar.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.local.AsteroidDatabase
import com.udacity.asteroidradar.data.local.entities.toDomain
import com.udacity.asteroidradar.data.remote.AsteroidsNeoWsService
import com.udacity.asteroidradar.data.remote.dto.AsteroidDto
import com.udacity.asteroidradar.data.remote.dto.toEntity
import com.udacity.asteroidradar.data.util.ConnectionState
import com.udacity.asteroidradar.data.util.JsonParser
import com.udacity.asteroidradar.data.util.NetworkResult
import com.udacity.asteroidradar.domain.model.Asteroid
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

enum class AsteroidsFilter {
    WEEK,
    TODAY,
    SAVED
}

class AsteroidsRepository(
    private val networkService: AsteroidsNeoWsService,
    private val asteroidDatabase: AsteroidDatabase,
    private val context: Context
) {

    private val _connectionState: MutableLiveData<ConnectionState> =
        MutableLiveData(ConnectionState.Connected())
    val connectionState: LiveData<ConnectionState> = _connectionState

    suspend fun refreshAsteroidDatabase() {
        when (val networkResult = fetchAsteroidsDataFromInternet()) {
            is NetworkResult.OnSuccess -> {
                val result = networkResult.data?.map {
                    it.toEntity()
                }?.toTypedArray()
                asteroidDatabase.asteroidDao.insertAll(* result!!)
            }
            is NetworkResult.OnError -> {
                _connectionState.postValue(ConnectionState.Disconnected(networkResult.errorMessage!!))
            }
        }
    }

//    suspend fun getPictureOfDayDto(): NetworkResult<PictureOfDay> {
//        return try {
//            val response = networkService.getPictureOfDayDto()
//            NetworkResult.OnSuccess(response.toDomain())
//        } catch (e: IOException) {
//            NetworkResult.OnError(errorMessage = context.getString(R.string.un_known_error))
//        } catch (e: HttpException) {
//            NetworkResult.OnError(errorMessage = context.getString(R.string.check_your_internet_connection))
//        }
//    }

    fun getAsteroid(filter: AsteroidsFilter = AsteroidsFilter.WEEK): LiveData<List<Asteroid>> {
        return when (filter) {
            AsteroidsFilter.WEEK -> {
                val start = JsonParser.getInstance().getNextSevenDays().first()
                val end = JsonParser.getInstance().getNextSevenDays().last()
                Transformations.map(
                    asteroidDatabase.asteroidDao.getWeekAsteroids(
                        start = start,
                        end = end
                    )
                ) {
                    it.toDomain()
                }
            }
            AsteroidsFilter.SAVED -> {
                Transformations.map(asteroidDatabase.asteroidDao.getAsteroids()) {
                    it.toDomain()
                }
            }
            AsteroidsFilter.TODAY -> {
                val today = JsonParser.getInstance().getNextSevenDays().first()
                Transformations.map(asteroidDatabase.asteroidDao.getTodayAsteroids(today)) {
                    it.toDomain()
                }
            }
        }
    }

    private suspend fun fetchAsteroidsDataFromInternet(): NetworkResult<List<AsteroidDto>> {
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