package com.example.weatherapp.repository

import android.content.Context
import android.util.Log
import com.example.weatherapp.api.PlacesApi
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.data.LocalForecast
import com.example.weatherapp.data.LocalLocation
import com.example.weatherapp.data.LocalWeather
import com.example.weatherapp.data.forecast.Forecast
import com.example.weatherapp.data.places.Places
import com.example.weatherapp.data.weather.CurrentWeather
import com.example.weatherapp.db.WeatherDao
import com.example.weatherapp.utils.convertToLocalForecast
import com.example.weatherapp.utils.convertToLocalWeather
import com.example.weatherapp.utils.isNetworkAvailable
import retrofit2.Response
import java.lang.Exception

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val placeApi: PlacesApi,
    private val weatherDao: WeatherDao,
): WeatherRepository {
    private val key = "88c0154a25fb21fb0d30003e6956fb4c"
    private val key2 = "fKzVotCk8CmC2teZ3QEn0f9XoL_sZAV585wgTMKFoao"
    override suspend fun getWeatherFromApi(label: String): Response<CurrentWeather> = weatherApi.getWeather(label,key)
    override suspend fun getForecastFromApi(label: String): Response<Forecast> = weatherApi.getForecast(label,key)
    override suspend fun getCitiesFromApi(pattern: String): Response<Places> = placeApi.getCities(pattern ,key2)
    override suspend fun UpdateWeatherInRoom(weather: LocalWeather) = weatherDao.upsertCityWeather(weather)
    override suspend fun getWeatherFromRoom(label: String): LocalWeather = weatherDao.getCityWeather(label)
    override suspend fun getCitiesFromRoom(): List<LocalLocation> = weatherDao.getAllCities()
}