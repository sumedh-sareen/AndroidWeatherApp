package com.example.androidweatherapp.network


import com.example.androidweatherapp.BuildConfig
import com.example.androidweatherapp.model.ChatRequest
import com.example.androidweatherapp.model.ChatResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Qualifier

// API call builder here
interface TogetherApi {
    @POST("v1/chat/completions")
    suspend fun getRecommendation(@Body request: ChatRequest): ChatResponse
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TogetherAiClient

@Module
@InstallIn(SingletonComponent::class)
class TogetherAiModule() {

    @Provides
    @TogetherAiClient
    fun provideHttpOkClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().addInterceptor{
            chain -> chain.proceed(chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.TOGETHER_API_KEY}")
                .build())
        }.addInterceptor(logging).build()
    }

    @Provides
    @TogetherAiClient
    fun provideTogetherAiRetrotfit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.together.xyz/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideHttpOkClient())
            .build()

    }

    @Provides
    fun provideTogetherAiApi(@TogetherAiClient retrofit: Retrofit): TogetherApi {
        return retrofit.create(TogetherApi::class.java)
    }
}