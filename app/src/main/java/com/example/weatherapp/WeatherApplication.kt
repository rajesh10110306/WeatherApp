package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.api.RetrofitHelper
import com.example.weatherapp.api.RetrofitHelper2
import com.example.weatherapp.db.WeatherDatabase
import com.example.weatherapp.repository.WeatherRepository

class WeatherApplication: Application() {
    companion object{
        lateinit var weatherRepository: WeatherRepository
    }

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    fun initialize(){
        val api = RetrofitHelper.api
        val api2 = RetrofitHelper2.api
        val database = WeatherDatabase(applicationContext)
        weatherRepository = WeatherRepository(api,api2,database,applicationContext)
    }
}