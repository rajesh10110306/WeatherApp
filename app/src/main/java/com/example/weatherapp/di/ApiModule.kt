package com.example.weatherapp.di

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.api.PlacesApi
import com.example.weatherapp.api.WeatherApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val WEATHER_API_BASE_URL = BuildConfig.BASE_URL
val PLACES_API_BASE_URL = BuildConfig.BASE_URL2

val apiModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(WEATHER_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    single {
        Retrofit.Builder()
            .baseUrl(PLACES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacesApi::class.java)
    }
}