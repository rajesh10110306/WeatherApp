package com.example.weatherapp.model.data

import androidx.room.Entity

@Entity(primaryKeys = ["lat","lon"])
data class LocalCity (
    val lat: Double,
    val lon: Double,
    val city:String,
    val state:String,
    val country:String
    )