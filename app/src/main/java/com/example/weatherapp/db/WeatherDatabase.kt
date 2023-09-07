package com.example.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.data.LocalWeather

@Database(entities = [LocalWeather::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao
}