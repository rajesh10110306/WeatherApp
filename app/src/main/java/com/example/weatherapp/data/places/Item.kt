package com.example.weatherapp.data.places

data class Item(
    val address: com.example.weatherapp.data.places.Address,
    val highlights: com.example.weatherapp.data.places.Highlights,
    val houseNumberType: String,
    val id: String,
    val language: String,
    val resultType: String,
    val title: String
)