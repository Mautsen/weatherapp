package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    val current: Current
)

data class Current(
    @SerializedName("temperature_2m") val temperature: Double
)
