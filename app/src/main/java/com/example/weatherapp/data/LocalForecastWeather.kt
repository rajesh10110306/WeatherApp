package com.example.weatherapp.data

data class LocalForecastWeather (
    val label: String,
    val city: String?,
    val update_time: Int,
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int,
    val feels_like: Double,
    val pressure: Int,
    val speed: Double,
    val status: String,
    val icon: String,
    val sunrise: Int,
    val sunset: Int,
    val avgTemp: Double,
    val avgHumidity: Int,
    val avgPressure: Int,
)