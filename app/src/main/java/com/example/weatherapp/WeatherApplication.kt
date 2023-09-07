package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.api.WeatherBuilder
import com.example.weatherapp.api.PlaceBuilder
import com.example.weatherapp.db.WeatherDatabase
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.repository.WeatherRepositoryImpl

class WeatherApplication: Application() {
    companion object{
        lateinit var weatherRepository: WeatherRepository
    }

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    fun initialize(){
        val weatherApi = WeatherBuilder.api
        val placeApi = PlaceBuilder.api
        val weatherDao = WeatherDatabase(applicationContext).getWeatherDao()
        weatherRepository = WeatherRepositoryImpl(weatherApi,placeApi,weatherDao,applicationContext)
    }
}