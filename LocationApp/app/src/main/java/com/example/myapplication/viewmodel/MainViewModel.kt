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
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val locationRepository = LocationRepository(application)
    private val weatherRepository = WeatherRepository()

    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?> = _location

    private val _weather = MutableLiveData<CurrentWeatherResponse>()
    val weather: LiveData<CurrentWeatherResponse> = _weather

    init {
        startLocationUpdates()
    }

    fun startLocationUpdates() {
        locationRepository.startLocationUpdates { location ->
            _location.value = location
            Log.i("MainViewModel", "Received location: $location")
            fetchWeather(location)
        }
    }

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
}


