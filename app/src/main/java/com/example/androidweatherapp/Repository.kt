package com.example.androidweatherapp


import android.util.Log
import com.example.androidweatherapp.model.ChatRequest
import com.example.androidweatherapp.model.Message
import com.example.androidweatherapp.model.WeatherResponse
import com.example.androidweatherapp.network.GeoNamesApi
import com.example.androidweatherapp.network.TogetherApi
import com.example.androidweatherapp.network.WeatherApi
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

            val geoNamesResponse = geoNamesApi.getPlaceDetails(q = address, username = BuildConfig.GEONAMES_USERNAME, maxRows = 1)

            // use lat and long values - THESE VALUES ARE EXAMPLE ONLY. NEED TO MODIFY THE BELOW CODE
            val response = weatherApi.getCurrentWeather(latitude = geoNamesResponse.geonames[0].lat.toDouble(), longitude = geoNamesResponse.geonames[0].lng.toDouble(), apiKey = BuildConfig.OPENWEATHER_API_KEY)
            return response
    }


}

// object since this is a singleton instantiation and will only be used ONCE per AI recommendation
object WeatherPromptBuilder {
    fun build(weather: WeatherResponse): String {
        val current = weather.current
        val hourly = weather.hourly.take(6) // next 6 hours

        val summary = StringBuilder()
        summary.append("Current temperature: ${current.temp}°C, feels like ${current.feels_like}°C.\n")
        summary.append("Conditions: ${current.weather[0].description}.\n")
        summary.append("Wind speed: ${current.wind_speed} km/h.\n")
        summary.append("Forecast for next 6 hours:\n")
        hourly.forEach { // append information about each hour to the summary string
            summary.append("- Hour ${it.dt}: ${it.temp}°C, ${it.weather[0].description}\n")
        }

        return """
            You are a weather-aware clothing and travel advisor. Based on the following data, suggest what to wear and how to stay safe:
            
            $summary
            
            Give advice in 2–3 sentences.
        """.trimIndent()



    }
}

// separation of concerns so creating a separate ai suggestion class
@Singleton
class AiSuggestionRepository @Inject constructor(private val togetherApi: TogetherApi) {
    // handling AI API tokens
    private var tokenCount = 0
    private val TOKEN_LIMIT = 100_000

    suspend fun getAiSuggestions(weather: WeatherResponse): String {
        if(tokenCount >= TOKEN_LIMIT) {
            return "AI suggestion limit reached for this month. Please try again later."
        }
        val prompt = WeatherPromptBuilder.build(weather)
        val request = ChatRequest(
            model = "mistralai/Mixtral-8x7B-Instruct-v0.1",
            messages = listOf(Message(role = "user", content = prompt)),
            max_tokens = 150,
            temperature = 0.7
        )

        val response = togetherApi.getRecommendation(request)
        tokenCount += response.usage?.totalTokens ?: 0

        return response.choices.firstOrNull()?.message?.content ?: "No suggestion available."

    }
}