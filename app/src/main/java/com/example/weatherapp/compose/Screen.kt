package com.example.weatherapp.compose

sealed class Screen(val route: String) {
    object WeatherInfoScreen: Screen("WeatherInfoScreen")
    object CitySearchScreen: Screen("CitySearchScreen")
}
