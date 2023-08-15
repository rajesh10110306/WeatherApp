package com.example.weatherapp.model.interfaces

import com.example.weatherapp.model.data.CityList
import com.example.weatherapp.model.data.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CustomApi {
    @GET
    suspend fun getCurrentWeather(@Url url:String) : Response<CurrentWeather>

    @GET
    suspend fun getCity(@Url url:String) : Response<CityList>
}