package com.udacity.asteroidradar.data.util

sealed class ConnectionState(val message:String?= null) {
    class Connected():ConnectionState()
    class Disconnected(message:String):ConnectionState(message = message)
}