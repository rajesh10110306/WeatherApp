package com.example.weatherapp.data.weather

data class CurrentWeather(
    val base: String,
    val clouds: com.example.weatherapp.data.weather.Clouds,
    val cod: Int,
    val coord: com.example.weatherapp.data.weather.Coord,
    val dt: Int,
    val id: Int,
    val main: com.example.weatherapp.data.weather.Main,
    val name: String,
    val rain: com.example.weatherapp.data.weather.Rain,
    val sys: com.example.weatherapp.data.weather.Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<com.example.weatherapp.data.weather.Weather>,
    val wind: com.example.weatherapp.data.weather.Wind
)