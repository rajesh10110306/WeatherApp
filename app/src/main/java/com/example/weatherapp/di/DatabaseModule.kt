package com.example.weatherapp.di

import android.app.Application
import androidx.room.Room
import com.example.weatherapp.db.WeatherDao
import com.example.weatherapp.db.WeatherDatabase
import org.koin.dsl.module

val databaseModule = module {
    fun getWeatherDatabase(application: Application): WeatherDatabase {
        return Room.databaseBuilder(
            application,
            WeatherDatabase::class.java,
            "weather_database.db"
        ).build()
    }

    fun getWeatherDao(weatherDatabase: WeatherDatabase): WeatherDao {
        return weatherDatabase.getWeatherDao()
    }

    single {
        getWeatherDatabase(get())
    }
    single {
        getWeatherDao(get())
    }
}