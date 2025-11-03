package com.example.androidweatherapp.network

import com.example.androidweatherapp.model.WeatherResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Qualifier


interface WeatherApi {
    @GET("onecall") // relative URL
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String = "minutely,hourly,daily,alerts",
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): WeatherResponse // need a suspension function for asynchronously running this network api call
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherClient

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    @Provides
    @WeatherClient
    fun provideHttpOkClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // FYI alternative way to write the above
        // private val logging = HttpLoggingInterceptor()
        //logging.level = HttpLoggingInterceptor.Level.BODY

        // return OkHttpClient with logging enabled
        return OkHttpClient.Builder().addInterceptor(logging).build()

    }

    @Provides
    @WeatherClient
    // empower the API interface with retrofit
    fun provideWeatherRetrofit(@WeatherClient client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/3.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    // retrofit enabled interface provided here
    fun provideWeatherApi(@WeatherClient retrofit: Retrofit): WeatherApi { // even though we are just referencing WeatherApi return type here, Hilt automatically creates the object
        // on the dependency injection within the repository
        return retrofit.create(WeatherApi::class.java)
    }
}






