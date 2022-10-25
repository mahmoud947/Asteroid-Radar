package com.udacity.asteroidradar.data.util

/*
* this class will represent api response
* to detect
* on error
* on success
* on loading
 */
sealed class NetworkResult<T>(val data: T? = null, val errorMessage: String? = null) {
    class OnSuccess<T>(data: T) : NetworkResult<T>(data = data)
    class OnLoading<T> : NetworkResult<T>()
    class OnError<T>(errorMessage: String) : NetworkResult<T>(errorMessage = errorMessage)
}
