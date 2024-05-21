package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    val current: Current
)

data class Current(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("weather_code") val weatherCode: Int
)

data class DailyForecastResponse(
    val daily: DailyForecast
)

data class DailyForecast(
    val time: List<String>,
    @SerializedName("temperature_2m_max") val maxTemperatures: List<Double>,
    @SerializedName("temperature_2m_min") val minTemperatures: List<Double>,
    @SerializedName("weather_code") val weatherCodes: List<Int>,
    @SerializedName("uv_index_max") val uvIndexes: List<Double>,
    @SerializedName("precipitation_sum") val rainSum: List<Double>
)
