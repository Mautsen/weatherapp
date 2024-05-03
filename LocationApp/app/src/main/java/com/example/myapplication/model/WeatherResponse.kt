package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("temperature_2m") val temperature: Double,

)
