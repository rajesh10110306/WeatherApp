package com.example.weatherapp.api

import com.example.weatherapp.data.forecast.Forecast
import com.example.weatherapp.data.weather.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ) : Response<CurrentWeather>

    @GET("forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Response<Forecast>
}