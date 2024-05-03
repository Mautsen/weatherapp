package com.example.myapplication.model

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String,
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse
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

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse {
        val defaultHourlyParam = "temperature_2m"
        val weatherResponse = openMeteoApi.getWeather(latitude, longitude, defaultHourlyParam)
        Log.d("WeatherResponse", weatherResponse.toString())
        return weatherResponse
    }

}

