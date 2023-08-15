package com.example.weatherapp.model.data

import androidx.room.Entity

@Entity(primaryKeys= [ "lat","lon" ] )
data class LocalWeather (
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int,
    val feels_like: Double,
    val lat: Double,
    val lon: Double,
    val city:String,
    val state:String,
    val country:String
    )
