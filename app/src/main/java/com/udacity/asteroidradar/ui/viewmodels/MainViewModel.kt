package com.udacity.asteroidradar.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.Constants
import com.udacity.asteroidradar.data.local.AsteroidDatabase
import com.udacity.asteroidradar.data.remote.AsteroidNewWsApi
import com.udacity.asteroidradar.data.util.NetworkResult
import com.udacity.asteroidradar.domain.model.Asteroid
import com.udacity.asteroidradar.domain.model.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    appContext: Context
) : ViewModel() {
    private val repository: AsteroidsRepository = AsteroidsRepository(
        AsteroidNewWsApi.getInstance(),
        AsteroidDatabase.getInstance(context = appContext),
        appContext
    )

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.refreshAsteroidDatabase()
                handelPictureOfDay()
            }
        }
    }

    val asteroids = repository.asteroids

    private val _navigateToDetail: MutableLiveData<Asteroid> = MutableLiveData()
    val navigateToDetail: LiveData<Asteroid> get() = _navigateToDetail

    private val _pictureOfDay: MutableLiveData<PictureOfDay> = MutableLiveData()
    val pictureOfDay: LiveData<PictureOfDay> get() = _pictureOfDay


    private suspend fun handelPictureOfDay() {
        val result = repository.getPictureOfDayDto()
        when (result) {
            is NetworkResult.OnSuccess -> {
                val data = result.data
                if (data?.mediaType == Constants.IMAGE_MEDIA_TYPE)
                    _pictureOfDay.postValue(result.data)
            }
            is NetworkResult.OnError -> {

            }
        }
    }

    fun onNavigateToDetail(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun onNavigateComplete() {
        _navigateToDetail.value = null
    }


    class Factory(private val appContext: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(appContext = appContext) as T
            }
            throw IllegalArgumentException("Unable to construct view-model")
        }

    }
}



