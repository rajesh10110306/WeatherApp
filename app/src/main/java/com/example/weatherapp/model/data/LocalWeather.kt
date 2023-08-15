package com.example.weatherapp.model.data

import androidx.room.Entity

@Entity(primaryKeys= ["label"] )
data class LocalWeather (
    val label: String,
    val city: String?,
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int,
    val feels_like: Double,
)