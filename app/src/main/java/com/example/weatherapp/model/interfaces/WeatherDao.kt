package com.example.weatherapp.model.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.model.data.LocalCity
import com.example.weatherapp.model.data.LocalWeather

@Dao
interface WeatherDao {
    @Insert
    suspend fun insertCityWeather(weather: LocalWeather)

    @Query("SELECT * FROM localweather WHERE lat=:lat AND lon=:lon")
    suspend fun getCityWeather(lat:Double, lon:Double) : LocalWeather

    @Insert
    suspend fun insertCity(localCity: ArrayList<LocalCity>)

    @Query("SELECT * FROM localcity WHERE city=:city")
    suspend fun getCity(city:String) : List<LocalCity>
}