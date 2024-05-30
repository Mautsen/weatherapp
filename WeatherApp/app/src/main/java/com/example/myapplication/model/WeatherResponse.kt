package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the current weather response from the Open Meteo API.
 *
 * @property current The current weather details.
 */
data class CurrentWeatherResponse(
    val current: Current
)

/**
 * Data class representing the current weather details.
 *
 * @property temperature The current temperature in degrees Celsius.
 * @property weatherCode The weather code representing the current weather condition.
 */
data class Current(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("weather_code") val weatherCode: Int
)

/**
 * Data class representing the daily forecast response from the Open Meteo API.
 *
 * @property daily The daily forecast details.
 */
data class DailyForecastResponse(
    val daily: DailyForecast
)

/**
 * Data class representing the daily forecast details.
 *
 * @property time A list of dates for the forecast.
 * @property maxTemperatures A list of maximum temperatures for each day.
 * @property minTemperatures A list of minimum temperatures for each day.
 * @property weatherCodes A list of weather codes representing the weather conditions for each day.
 * @property uvIndexes A list of UV index values for each day.
 * @property rainSum A list of total precipitation values for each day.
 * @property maxWind A list of maximum wind speed values for each day.
 */
data class DailyForecast(
    val time: List<String>,
    @SerializedName("temperature_2m_max") val maxTemperatures: List<Double>,
    @SerializedName("temperature_2m_min") val minTemperatures: List<Double>,
    @SerializedName("weather_code") val weatherCodes: List<Int>,
    @SerializedName("uv_index_max") val uvIndexes: List<Double>,
    @SerializedName("precipitation_sum") val rainSum: List<Double>,
    @SerializedName("wind_speed_10m_max") val maxWind: List<Double>
)
