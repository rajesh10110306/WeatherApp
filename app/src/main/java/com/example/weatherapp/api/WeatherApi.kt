package com.example.weatherapp.api

import com.example.weatherapp.data.forecast.Forecast
import com.example.weatherapp.data.places.Places
import com.example.weatherapp.data.weather.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ) : Response<com.example.weatherapp.data.weather.CurrentWeather>

    @GET("forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Response<com.example.weatherapp.data.forecast.Forecast>

    @GET("autocomplete")
    suspend fun getCity(
        @Query("q") pattern: String,
        @Query("apiKey") apiKey: String
    ) : Response<com.example.weatherapp.data.places.Places>
}