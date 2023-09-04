package com.example.weatherapp.utils

import com.example.weatherapp.data.LocalForecast
import com.example.weatherapp.data.LocalForecastWeather
import com.example.weatherapp.data.LocalWeather
import com.example.weatherapp.data.forecast.Forecast
import com.example.weatherapp.data.weather.CurrentWeather
import kotlin.math.round

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

fun convertToLocalForecastWeather(data: LocalWeather,data2: List<LocalForecast>): LocalForecastWeather{
    var avgTemp = 0.0
    var avgPressure = 0.0
    var avgHumidity = 0.0
    var count = 0
    for (i in data2){
        count++
        avgTemp+=i.temp
        avgPressure+=i.pressure
        avgHumidity+=i.humidity
    }
    return LocalForecastWeather(
        data.label,
        data.city,
        data.update_time,
        data.temp,
        data.temp_min,
        data.temp_max,
        data.humidity,
        data.feels_like,
        data.pressure,
        data.speed,
        data.status,
        data.icon,
        data.sunrise,
        data.sunset,
        round((avgTemp/count) * 100) / 100,
        (avgPressure/count).toInt(),
        (avgHumidity/count).toInt()
    )
}