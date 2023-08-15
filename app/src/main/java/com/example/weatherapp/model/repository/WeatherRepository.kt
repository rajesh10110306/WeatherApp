package com.example.weatherapp.model.repository

import com.example.weatherapp.model.data.LocalLocation
import com.example.weatherapp.model.data.LocalWeather
import com.example.weatherapp.model.interfaces.WeatherDatabase

class WeatherRepository(private val weatherDatabase: WeatherDatabase) {
    suspend fun upsertCityWeather(weather: LocalWeather) = weatherDatabase.getWeatherDao().upsertCityWeather(weather)
    suspend fun getCityWeather(label:String):LocalWeather = weatherDatabase.getWeatherDao().getCityWeather(label)
    suspend fun getAllCities():List<LocalLocation> = weatherDatabase.getWeatherDao().getAllCities()
}
