package com.example.weatherapp.data.forecast

data class ForecastData(
    val clouds: com.example.weatherapp.data.forecast.Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: com.example.weatherapp.data.forecast.Main,
    val pop: Int,
    val sys: com.example.weatherapp.data.forecast.Sys,
    val visibility: Int,
    val weather: List<com.example.weatherapp.data.forecast.Weather>,
    val wind: com.example.weatherapp.data.forecast.Wind
)