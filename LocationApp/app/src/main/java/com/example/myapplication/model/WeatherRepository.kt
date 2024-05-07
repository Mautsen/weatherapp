package com.example.myapplication.model

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import android.location.Location


interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String,
        @Query("timezone") timezone: String = "auto"
    ): CurrentWeatherResponse
}

class WeatherRepository {
    private val openMeteoApi: OpenMeteoApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        openMeteoApi = retrofit.create(OpenMeteoApi::class.java)
    }

    suspend fun getWeather(latitude: Double, longitude: Double): CurrentWeatherResponse {
        val currentParam = "temperature_2m,weather_code"
        val weatherResponse = openMeteoApi.getWeather(latitude, longitude, currentParam)
        Log.d("WeatherResponse", "Weather code: ${weatherResponse.current.weatherCode}")
        Log.d("WeatherResponse", "Current temperature: ${weatherResponse.current.temperature}")
        return weatherResponse
    }


}

