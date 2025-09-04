package com.example.androidweatherapp


import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityRepository @Inject constructor() {
    private val cityList = listOf("Melbourne", "Adelaide", "Perth", "Sydney", "Darwin", "Canberra")

    fun getCities():  List<String>{
        return cityList
    }
}

@Singleton
class WeatherRepository @Inject constructor() {
    val dataAcceptable = false


    fun validation(address: String) {
        // if validation successful, send the address to the weather API
        Log.d("repo", "validation in 'WeatherRepository()' $address")

    }


    fun getWeatherData(address: String) {
        Log.d("repo", "send data to openweatherAPI in 'WeatherRepository()' $address")
        

    }


}

// separation of concerns so creating a separate ai suggestion class
@Singleton
class AiSuggestionRepository @Inject constructor() {
    fun getAiSuggestions(address: String) {
        Log.d("repo", "send data to Ai suggestion API in 'WeatherRepository()' $address")
    }
}