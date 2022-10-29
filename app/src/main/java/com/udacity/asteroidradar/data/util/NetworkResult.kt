package com.udacity.asteroidradar.data.util

/*
* this class will represent api response
* to detect
* on error
* on success
* on loading
 */
sealed class NetworkResult<T>(val data: T? = null, val resErrorMessage: Int? = null) {
    class OnSuccess<T>(data: T) : NetworkResult<T>(data = data)
    class OnError<T>(resErrorMessage: Int) : NetworkResult<T>(resErrorMessage = resErrorMessage)
}
