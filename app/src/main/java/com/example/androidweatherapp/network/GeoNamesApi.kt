package com.example.androidweatherapp.network

import com.example.androidweatherapp.model.GeoNamesResponse
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

// deals wih making an API call to the places API to get the latitude and longitude values of a place
interface GeoNamesApi {
    @GET("searchJSON")
    suspend fun getPlaceDetails(
        @Query("q") q: String,
        @Query("maxRows") maxRows: Int = 1,
        @Query("username") username: String
    ): GeoNamesResponse
}

@Module
@InstallIn(SingletonComponent::class)
object GeoNamesModule {

    @Provides
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
    // empower the API interface with retrofit
    fun provideGeoNamesRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://api.geonames.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideGeoNamesApi(retrofit: Retrofit): GeoNamesApi {
        return retrofit.create(GeoNamesApi::class.java)
    }
}




