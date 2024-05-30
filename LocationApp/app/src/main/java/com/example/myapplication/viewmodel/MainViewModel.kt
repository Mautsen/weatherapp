package com.example.myapplication.viewmodel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.LocationRepository
import com.example.myapplication.model.WeatherRepository
import com.example.myapplication.model.CurrentWeatherResponse
import com.example.myapplication.model.DailyForecastResponse
import kotlinx.coroutines.launch

/**
 * ViewModel class for the main view of the application, responsible for the background logic.
 *
 * @param application The application instance.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val locationRepository = LocationRepository(application)
    private val weatherRepository = WeatherRepository()

    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?> = _location

    private val _selectedCity = MutableLiveData<String?>()
    val selectedCity: LiveData<String?> = _selectedCity

    private val _weather = MutableLiveData<CurrentWeatherResponse>()
    val weather: LiveData<CurrentWeatherResponse> = _weather

    private val _dailyForecast = MutableLiveData<DailyForecastResponse>()
    val dailyForecast: LiveData<DailyForecastResponse> = _dailyForecast

    private val _isCelsius = MutableLiveData(true)
    val isCelsius: LiveData<Boolean> = _isCelsius

    init {
        startLocationUpdates()
    }

    /**
     * Starts location updates using the LocationRepository.
     * If no city is selected, updates the current location and fetches the weather data.
     */
    fun startLocationUpdates() {
        locationRepository.startLocationUpdates { location ->
            if (_selectedCity.value == null) {
                _location.value = location
                fetchWeather(location)
                fetchDailyForecast(location)
            }
        }
    }

    /**
     * Fetches the current weather data for the given location.
     *
     * @param location The location for which to fetch weather data.
     */
    private fun fetchWeather(location: Location?) {
        location?.let { loc ->
            viewModelScope.launch {
                try {
                    val weather = weatherRepository.getWeather(loc.latitude, loc.longitude)
                    _weather.value = weather
                } catch (e: Exception) {
                    Log.e("MainViewModel", "Error fetching weather", e)
                }
            }
        }
    }

    /**
     * Fetches the daily forecast data for the given location.
     *
     * @param location The location for which to fetch daily forecast data.
     */
    private fun fetchDailyForecast(location: Location?) {
        location?.let { loc ->
            viewModelScope.launch {
                try {
                    val dailyForecast = weatherRepository.getDailyForecast(loc.latitude, loc.longitude)
                    _dailyForecast.value = dailyForecast
                } catch (e: Exception) {
                    Log.e("MainViewModel", "Error fetching daily forecast", e)
                }
            }
        }
    }

    /**
     * Sets a custom location and fetches the weather and daily forecast data for it.
     *
     * @param lat The latitude of the custom location.
     * @param lon The longitude of the custom location.
     */
    fun setCustomLocation(lat: Double, lon: Double) {
        val location = Location("").apply {
            latitude = lat
            longitude = lon
        }
        _location.value = location
        _selectedCity.value = "Custom"
        fetchWeather(location)
        fetchDailyForecast(location)
    }

    /**
     * Uses the current GPS location by clearing the selected city and starting location updates.
     */
    fun useCurrentLocation() {
        _selectedCity.value = null
        startLocationUpdates()
    }

    /**
     * Toggles the temperature units between Fahrenheit and Celsius.
     *
     * This function inverts the current state of the `_isCelsius` LiveData, switching between
     * `true` (Celsius) and `false` (Fahrenheit). It is typically called when the user wants to
     * change the temperature unit displayed in the application.
     */
    fun toggleTemperatureUnit() {
        _isCelsius.value = _isCelsius.value != true
    }
}
