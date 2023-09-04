package com.example.weatherapp.data.forecast

data class City(
    val coord: com.example.weatherapp.data.forecast.Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)