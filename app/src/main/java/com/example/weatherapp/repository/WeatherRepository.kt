package com.example.weatherapp.repository

import com.example.weatherapp.data.LocalForecast
import com.example.weatherapp.data.LocalLocation
import com.example.weatherapp.data.LocalWeather

interface WeatherRepository {
    suspend fun getCurrentWeather(label: String, city: String?): Response<LocalWeather>
    suspend fun getForecast(label:String, city:String?): Response<List<LocalForecast>>
    suspend fun getCity(pattern:String) : Response<List<LocalLocation>>
}