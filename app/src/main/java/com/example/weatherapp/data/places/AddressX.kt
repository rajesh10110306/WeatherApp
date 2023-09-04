package com.example.weatherapp.data.places

data class AddressX(
    val city: List<com.example.weatherapp.data.places.City>,
    val houseNumber: List<com.example.weatherapp.data.places.HouseNumber>,
    val label: List<com.example.weatherapp.data.places.Label>,
    val street: List<com.example.weatherapp.data.places.Street>
)