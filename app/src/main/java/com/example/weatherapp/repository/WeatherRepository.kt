package com.example.weatherapp.repository

import com.example.weatherapp.data.LocalLocation
import com.example.weatherapp.data.LocalWeather
import com.example.weatherapp.data.forecast.Forecast
import com.example.weatherapp.data.places.Places
import com.example.weatherapp.data.weather.CurrentWeather
import retrofit2.Response

interface WeatherRepository {
    suspend fun getWeatherFromApi(label: String): Response<CurrentWeather>
    suspend fun getForecastFromApi(label:String): Response<Forecast>
    suspend fun getCitiesFromApi(pattern:String) : Response<Places>
    suspend fun UpdateWeatherInRoom(weather: LocalWeather)
    suspend fun getWeatherFromRoom(label:String) : LocalWeather
    suspend fun getCitiesFromRoom() : List<LocalLocation>
}