package com.udacity.asteroidradar.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.Constants
import com.udacity.asteroidradar.data.local.AsteroidDatabase
import com.udacity.asteroidradar.data.local.entities.toDomain
import com.udacity.asteroidradar.data.remote.AsteroidNewWsApi
import com.udacity.asteroidradar.data.remote.AsteroidsNeoWsService
import com.udacity.asteroidradar.data.remote.dto.toEntity
import com.udacity.asteroidradar.data.util.JsonParser
import com.udacity.asteroidradar.data.util.NetworkResult
import com.udacity.asteroidradar.domain.model.Asteroid
import com.udacity.asteroidradar.domain.model.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    private val asteroidDatabase: AsteroidDatabase
) {

    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(asteroidDatabase.pictureOfDayDao.getPictureOfTheDay()) { pictureOfDay ->
            pictureOfDay != null
            pictureOfDay?.toDomain()
        }

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

    suspend fun refreshAsteroidDatabase(): NetworkResult<Unit> {
        return try {
            val response = networkService.getAsteroids()
            val jsonObject = JSONObject(response)
            val asteroidsDto =
                JsonParser.getInstance().parseJsonResultAsAsteroid(jsonObject = jsonObject)
            asteroidDatabase.asteroidDao.insertAll(*asteroidsDto.toEntity())
            NetworkResult.OnSuccess(Unit)

        } catch (e: IOException) {
            NetworkResult.OnError(
                resErrorMessage = R.string.check_your_internet_connection
            )
        } catch (e: HttpException) {
            NetworkResult.OnError(
                resErrorMessage = R.string.server_error
            )
        } catch (e: Exception) {
            NetworkResult.OnError(
                resErrorMessage = R.string.un_known_error
            )
        }
    }

    suspend fun refreshPictureOfTheDay(): NetworkResult<Unit> {
        return try {
            val response = networkService.getPictureOfDay()
            if (response.mediaType == Constants.IMAGE_MEDIA_TYPE) {
                asteroidDatabase.pictureOfDayDao.deletePictureOfTheDay()
                asteroidDatabase.pictureOfDayDao.insertImage(response.toEntity())
            }
            NetworkResult.OnSuccess(Unit)
        } catch (e: IOException) {
            NetworkResult.OnError(
                resErrorMessage = R.string.check_your_internet_connection
            )
        } catch (e: HttpException) {
            NetworkResult.OnError(
                resErrorMessage = R.string.server_error
            )
        } catch (e: Exception) {
            NetworkResult.OnError(
                resErrorMessage = R.string.un_known_error
            )
        }
    }

    suspend fun deleteAsteroidsBeforeToday() {
        withContext(Dispatchers.IO){
            val today: String = JsonParser.getInstance().getNextSevenDays().first()
            asteroidDatabase.asteroidDao.deleteAsteroidsBeforeToday(day = today)
        }
    }

    companion object {
        private lateinit var INSTANCE: AsteroidsRepository
        fun getInstance(context: Context): AsteroidsRepository {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = AsteroidsRepository(
                    networkService = AsteroidNewWsApi.getInstance(),
                    asteroidDatabase = AsteroidDatabase.getInstance(context = context)
                )
            }
            return INSTANCE
        }
    }

}