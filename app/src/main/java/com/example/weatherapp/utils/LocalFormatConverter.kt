package com.example.weatherapp.utils

import com.example.weatherapp.data.LocalForecast
import com.example.weatherapp.data.LocalWeather
import com.example.weatherapp.data.forecast.Forecast
import com.example.weatherapp.data.weather.CurrentWeather

fun convertToLocalWeather(label: String,city: String?,data: CurrentWeather): LocalWeather {
    return LocalWeather(
        label,
        city,
        data.dt,
        data.main.temp,
        data.main.temp_min,
        data.main.temp_max,
        data.main.humidity,
        data.main.feels_like,
        data.main.pressure,
        data.wind.speed,
        data.weather[0].description,
        data.weather[0].icon,
        data.sys.sunrise,
        data.sys.sunset
    )
}
fun convertToLocalForecast(data: Forecast): List<LocalForecast>{
    val result = ArrayList<LocalForecast>()
    for (item in data.list){
        result.add(LocalForecast(item.weather[0].icon,item.weather[0].description,item.main.temp,item.main.humidity,item.main.pressure,item.dt_txt))
    }
    return result
}