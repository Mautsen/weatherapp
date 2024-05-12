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
// interface for 7 day forecast
interface DailyForecastApi {
    @GET("v1/forecast")
    suspend fun getDailyForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String,
        @Query("timezone") timezone: String = "auto"
    ): DailyForecastResponse
}

class WeatherRepository {
    private val openMeteoApi: OpenMeteoApi
    private val dailyForecastApi: DailyForecastApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        openMeteoApi = retrofit.create(OpenMeteoApi::class.java)
        dailyForecastApi = retrofit.create(DailyForecastApi::class.java)
    }

    suspend fun getWeather(latitude: Double, longitude: Double): CurrentWeatherResponse {
        val currentParam = "temperature_2m,weather_code"
        val weatherResponse = openMeteoApi.getWeather(latitude, longitude, currentParam)
        Log.d("WeatherResponse", "Weather code: ${weatherResponse.current.weatherCode}")
        Log.d("WeatherResponse", "Current temperature: ${weatherResponse.current.temperature}")
        return weatherResponse
    }
    suspend fun getDailyForecast(latitude: Double, longitude: Double): DailyForecastResponse {

        val dailyParam = "temperature_2m_max,temperature_2m_min,weather_code"
        val dailyResponse = dailyForecastApi.getDailyForecast(latitude, longitude, dailyParam)
        Log.d("WeatherResponse", "Weather code: ${dailyResponse.daily.weatherCodes}")
        return dailyResponse
    }


}

