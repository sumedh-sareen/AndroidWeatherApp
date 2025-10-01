package com.example.androidweatherapp


import android.util.Log
import com.example.androidweatherapp.model.GeoNamesResponse
import com.example.androidweatherapp.model.WeatherResponse
import com.example.androidweatherapp.network.GeoNamesApi
import com.example.androidweatherapp.network.GeoNamesModule
import com.example.androidweatherapp.network.WeatherApi
import com.example.androidweatherapp.network.WeatherModule
import retrofit2.http.GET
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
class WeatherRepository @Inject constructor(private val geoNamesApi: GeoNamesApi, private val weatherApi: WeatherApi) {

    fun validation(address: String) {
        // if validation successful, send the address to the weather API
        Log.d("repo", "validation in 'WeatherRepository()' $address")

    }


    suspend fun getWeatherData(address: String): WeatherResponse {
        // first use address information to convert it into lat and long values
        // add code here

            val geoNamesResponse = geoNamesApi.getPlaceDetails(q = "Melbourne", username = BuildConfig.GEONAMES_USERNAME, maxRows = 1)

            // use lat and long values - THESE VALUES ARE EXAMPLE ONLY. NEED TO MODIFY THE BELOW CODE
            val response = weatherApi.getCurrentWeather(latitude = geoNamesResponse.geonames[0].lat.toDouble(), longitude = geoNamesResponse.geonames[0].lng.toDouble(), apiKey = BuildConfig.OPENWEATHER_API_KEY)
            return response






    }


}

// separation of concerns so creating a separate ai suggestion class
@Singleton
class AiSuggestionRepository @Inject constructor() {
    fun getAiSuggestions(address: String) {
        Log.d("repo", "send data to Ai suggestion API in 'WeatherRepository()' $address")
    }
}