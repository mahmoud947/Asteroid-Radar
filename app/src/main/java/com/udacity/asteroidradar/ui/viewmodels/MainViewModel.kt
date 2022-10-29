package com.udacity.asteroidradar.ui.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.util.ConnectionState
import com.udacity.asteroidradar.data.util.NetworkResult
import com.udacity.asteroidradar.domain.model.Asteroid
import com.udacity.asteroidradar.repository.AsteroidsFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    appContext: Context
) : ViewModel() {

    private val repository: AsteroidsRepository = AsteroidsRepository.getInstance(appContext)

    private val _navigateToDetail: MutableLiveData<Asteroid> = MutableLiveData()
    val navigateToDetail: LiveData<Asteroid> get() = _navigateToDetail

    private val _filter: MutableLiveData<AsteroidsFilter> = MutableLiveData(AsteroidsFilter.TODAY)
    val asteroids = Transformations.switchMap(_filter) {
        repository.getAsteroid(it)
    }


    private val _connectionState: MutableLiveData<ConnectionState?> = MutableLiveData(null)
    val connectionState: LiveData<ConnectionState?> get() = _connectionState

    val pictureOfDay = repository.pictureOfDay


    private val _isError: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = asteroids.map {
            it.isEmpty() && _isError.value!!
        }

    init {
        viewModelScope.launch {
            handelError(repository.refreshAsteroidDatabase(), context = appContext)
        }
    }


    fun onNavigateToDetail(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun onNavigateComplete() {
        _navigateToDetail.value = null
    }

    fun updateFilter(filter: AsteroidsFilter) {
        _filter.value = filter
    }

    fun onUpdateConnectionStateCompleted() {
        _connectionState.value = null
    }


    private suspend fun handelError(networkResult: NetworkResult<Unit>, context: Context) {
        withContext(Dispatchers.IO) {
            when (networkResult) {

                is NetworkResult.OnError -> {
                    _connectionState.postValue(
                        ConnectionState.Disconnected(
                            message = context.getString(
                                networkResult.resErrorMessage!!
                            )
                        )
                    )
                    _isError.postValue(false)
                }
                is NetworkResult.OnSuccess -> {
                    repository.refreshPictureOfTheDay()
                    _connectionState.postValue(
                        ConnectionState.Connected(
                            message = context.getString(
                                R.string.you_are_online
                            )
                        )
                    )
                    _isError.postValue(false)
                }

            }
        }
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



