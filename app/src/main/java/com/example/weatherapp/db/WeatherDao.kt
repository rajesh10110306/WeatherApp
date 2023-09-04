package com.example.weatherapp.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.weatherapp.data.LocalLocation
import com.example.weatherapp.data.LocalWeather

@Dao
interface WeatherDao {
    @Upsert
    suspend fun upsertCityWeather(weather: LocalWeather)

    @Query("SELECT * FROM localweather WHERE label=:label")
    suspend fun getCityWeather(label:String) : LocalWeather

    @Query("SELECT label,city FROM localweather")
    suspend fun getAllCities() : List<LocalLocation>
}