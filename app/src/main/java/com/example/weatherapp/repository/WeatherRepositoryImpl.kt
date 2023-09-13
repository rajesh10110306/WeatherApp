package com.example.weatherapp.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.api.PlacesApi
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.data.LocalLocation
import com.example.weatherapp.data.LocalWeather
import com.example.weatherapp.data.forecast.Forecast
import com.example.weatherapp.data.places.Places
import com.example.weatherapp.data.weather.CurrentWeather
import com.example.weatherapp.db.WeatherDao
import retrofit2.Response

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val placeApi: PlacesApi,
    private val weatherDao: WeatherDao,
): WeatherRepository {
    private val WEATHER_API_KEY = BuildConfig.API_KEY
    private val PLACES_API_KEY = BuildConfig.API_KEY2
    override suspend fun getWeatherFromApi(label: String): Response<CurrentWeather> = weatherApi.getWeather(label,WEATHER_API_KEY)
    override suspend fun getForecastFromApi(label: String): Response<Forecast> = weatherApi.getForecast(label,WEATHER_API_KEY)
    override suspend fun getCitiesFromApi(pattern: String): Response<Places> = placeApi.getCities(pattern ,PLACES_API_KEY)
    override suspend fun UpdateWeatherInRoom(weather: LocalWeather) = weatherDao.upsertCityWeather(weather)
    override suspend fun getWeatherFromRoom(label: String): LocalWeather = weatherDao.getCityWeather(label)
    override suspend fun getCitiesFromRoom(): List<LocalLocation> = weatherDao.getAllCities()
}