package com.example.weatherapp.api

import com.example.weatherapp.data.places.Places
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("autocomplete")
    suspend fun getCities(
        @Query("q") pattern: String,
        @Query("apiKey") apiKey: String
    ) : Response<Places>
}