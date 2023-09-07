package com.example.weatherapp.di

import com.example.weatherapp.api.PlacesApi
import com.example.weatherapp.api.WeatherApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://autocomplete.search.hereapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacesApi::class.java)
    }
}