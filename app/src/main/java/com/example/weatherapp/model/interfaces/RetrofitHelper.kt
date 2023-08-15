package com.example.weatherapp.model.interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val baseUrl = "https://api.openweathermap.org/"
    val api: CustomApi by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CustomApi::class.java)
    }
}