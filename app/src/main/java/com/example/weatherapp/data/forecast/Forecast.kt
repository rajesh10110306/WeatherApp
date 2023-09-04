package com.example.weatherapp.data.forecast

data class Forecast(
    val city: com.example.weatherapp.data.forecast.City,
    val cnt: Int,
    val cod: String,
    val list: List<com.example.weatherapp.data.forecast.ForecastData>,
    val message: Int
)