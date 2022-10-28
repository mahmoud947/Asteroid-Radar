package com.udacity.asteroidradar.data.util

sealed class ConnectionState(val message:String?= null) {
    class Connected(message: String?):ConnectionState(message = message)
    class Disconnected(message:String):ConnectionState(message = message)
}