package com.example.weatherapp.model.repository

import com.example.weatherapp.model.data.LocalCity
import com.example.weatherapp.model.data.LocalWeather
import com.example.weatherapp.model.interfaces.WeatherDatabase


class WeatherRepository(private val weatherDatabase: WeatherDatabase) {
    suspend fun insertCityWeather(weather: LocalWeather) = weatherDatabase.getWeatherDao().insertCityWeather(weather)
    suspend fun getCityWeather(lat:Double, lon:Double):LocalWeather = weatherDatabase.getWeatherDao().getCityWeather(lat,lon)
    suspend fun insertCity(localCity: ArrayList<LocalCity>) = weatherDatabase.getWeatherDao().insertCity(localCity)
    suspend fun getCity(city:String):List<LocalCity> = weatherDatabase.getWeatherDao().getCity(city)
}