package com.udacity.asteroidradar.data.util

import android.annotation.SuppressLint
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.data.Constants
import com.udacity.asteroidradar.data.remote.dto.AsteroidDto
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class JsonParser {

    // get instance from moshi that will use in serialization from json
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * this function get nasa api result as jsonObject in it's parameter
     * and use getNextSevenDays function to get next seven day in ["yyy-MM-dd"] format
     * and get near_earth_objects using dynamic key (date in ["yyy-MM-dd"])
     * and using moshi Instance to convert from json object to AsteroidDto
     * then it's return List of AsteroidDto
     */
     fun parseJsonResultAsAsteroid(jsonObject: JSONObject): List<AsteroidDto> {
        Log.i("object", jsonObject.toString())//Todo:Use Temper
        val days = getNextSevenDays()
        val nearEarthObjectsJson = jsonObject.getJSONObject("near_earth_objects")
        val asteroids: ArrayList<AsteroidDto> = arrayListOf()
        for (day in days) {
            if (nearEarthObjectsJson.has(day)) {
                val asteroidAsJsonArray = nearEarthObjectsJson.getJSONArray(day)
                val jsonAdapter: JsonAdapter<AsteroidDto> = moshi.adapter(AsteroidDto::class.java)
                for (i in 0 until asteroidAsJsonArray.length()) {
                    val asteroidDto: AsteroidDto? =
                        jsonAdapter.fromJson(asteroidAsJsonArray[i].toString())
                    asteroidDto?.let {
                        asteroids.add(it)
                    }
                }
            }
        }
        return asteroids
    }


    @SuppressLint("SimpleDateFormat")
     fun getNextSevenDays(): ArrayList<String> {
        val days: ArrayList<String> = arrayListOf()
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
        for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
            val calender: Calendar = GregorianCalendar()
            calender.add(Calendar.DATE, i)
            val dayAsString: String = simpleDateFormat.format(calender.time)
            days.add(dayAsString)
        }
        return days
    }

    // singleton pattern
    companion object {
        private lateinit var INSTANCE: JsonParser
        fun getInstance(): JsonParser {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = JsonParser()
            }
            return INSTANCE
        }
    }

}