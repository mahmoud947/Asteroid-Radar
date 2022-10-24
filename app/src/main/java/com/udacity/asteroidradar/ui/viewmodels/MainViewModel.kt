package com.udacity.asteroidradar.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.remote.AsteroidNewWsApi
import com.udacity.asteroidradar.data.remote.AsteroidsNeoWsService
import com.udacity.asteroidradar.data.util.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainViewModel : ViewModel() {
   private val api: AsteroidsNeoWsService = AsteroidNewWsApi.getInstance()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
               val response = api.getAsteroids()
              val json = JSONObject(response)
               val finalRespons = JsonParser.getInstance().parseJsonResultAsAsteroid(json)
               Log.i("response",finalRespons.toString())
            }
        }
    }




}