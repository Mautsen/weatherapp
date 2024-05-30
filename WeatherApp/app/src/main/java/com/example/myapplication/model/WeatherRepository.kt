package com.example.myapplication.model

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException

/**
 * Interface for fetching current weather data from Open Meteo API.
 */
interface OpenMeteoApi {
    /**
     * Fetches current weather data.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param current The current weather parameters to fetch.
     * @param timezone The timezone for the weather data (default is "auto").
     * @return A [CurrentWeatherResponse] containing the current weather data.
     */
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String,
        @Query("timezone") timezone: String = "auto"
    ): CurrentWeatherResponse
}

/**
 * Interface for fetching daily forecast data from Open Meteo API.
 */
interface DailyForecastApi {
    /**
     * Fetches daily forecast data for 7 days.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param daily The daily forecast parameters to fetch.
     * @param timezone The timezone for the weather data (default is "auto").
     * @return A [DailyForecastResponse] containing the daily forecast data.
     */
    @GET("v1/forecast")
    suspend fun getDailyForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String,
        @Query("timezone") timezone: String = "auto"
    ): DailyForecastResponse
}

/**
 * Repository class for handling weather data retrieval from Open Meteo API.
 */
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

    /**
     * Fetches the current weather for the given latitude and longitude.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return A [CurrentWeatherResponse] containing the current weather data, or null if an error occurs.
     */
    suspend fun getWeather(latitude: Double, longitude: Double): CurrentWeatherResponse? {
        return try {
            val currentParam = "temperature_2m,weather_code"
            val weatherResponse = openMeteoApi.getWeather(latitude, longitude, currentParam)
            Log.d("WeatherResponse", "Weather code: ${weatherResponse.current.weatherCode}")
            Log.d("WeatherResponse", "Current temperature: ${weatherResponse.current.temperature}")
            weatherResponse
        } catch (e: IOException) {
            Log.e("WeatherRepository", "Network error while fetching current weather", e)
            null
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error while fetching current weather", e)
            null
        }
    }

    /**
     * Fetches the daily weather forecast for the given latitude and longitude.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return A [DailyForecastResponse] containing the daily forecast data, or null if an error occurs.
     */
    suspend fun getDailyForecast(latitude: Double, longitude: Double): DailyForecastResponse? {
        return try {
            val dailyParam = "temperature_2m_max,temperature_2m_min,weather_code,uv_index_max,precipitation_sum,wind_speed_10m_max"
            val dailyResponse = dailyForecastApi.getDailyForecast(latitude, longitude, dailyParam)
            Log.d("WeatherResponse", "Weather code: ${dailyResponse.daily.weatherCodes}")
            dailyResponse
        } catch (e: IOException) {
            Log.e("WeatherRepository", "Network error while fetching daily forecast", e)
            null
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error while fetching daily forecast", e)
            null
        }
    }
}
