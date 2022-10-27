package com.udacity.asteroidradar.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.Constants
import com.udacity.asteroidradar.data.local.AsteroidDatabase
import com.udacity.asteroidradar.data.remote.AsteroidNewWsApi
import com.udacity.asteroidradar.data.util.NetworkResult
import com.udacity.asteroidradar.domain.model.Asteroid
import com.udacity.asteroidradar.domain.model.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidsFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

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
                repository.refreshPictureOfTheDay()
            }
        }
    }


    private val _navigateToDetail: MutableLiveData<Asteroid> = MutableLiveData()
    val navigateToDetail: LiveData<Asteroid> get() = _navigateToDetail

    private val _filter: MutableLiveData<AsteroidsFilter> = MutableLiveData(AsteroidsFilter.TODAY)

    val asteroids = Transformations.switchMap(_filter) {
        repository.getAsteroid(it)
    }
    val pictureOfDay = repository.pictureOfDay


    fun onNavigateToDetail(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun onNavigateComplete() {
        _navigateToDetail.value = null
    }

    fun updateFilter(filter: AsteroidsFilter) {
        _filter.value = filter
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



