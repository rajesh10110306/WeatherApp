package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.LocalForecast
import com.example.weatherapp.data.LocalLocation
import com.example.weatherapp.data.LocalWeather
import com.example.weatherapp.repository.Response
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository): ViewModel(){
    private val _weather = MutableSharedFlow<Response<LocalWeather>>(1)
    val weather: SharedFlow<Response<LocalWeather>>
        get() = _weather

    private val _forecast = MutableSharedFlow<Response<List<LocalForecast>>>(1)
    val forecast: SharedFlow<Response<List<LocalForecast>>>
        get() = _forecast

    private val _place = MutableSharedFlow<Response<List<LocalLocation>>>(1)
    val place: SharedFlow<Response<List<LocalLocation>>>
        get() = _place

    fun getWeather(label: String, city: String?){
        viewModelScope.launch {
            val weatherResponse = repository.getCurrentWeather(label,city)
            _weather.emit(weatherResponse)
        }
    }

    fun getForecast(label: String, city: String?){
        viewModelScope.launch {
            val forecastResponse = repository.getForecast(label,city)
            _forecast.emit(forecastResponse)
        }
    }

    fun getCity(pattern: String){
        viewModelScope.launch {
            val citiesResponse = repository.getCity(pattern)
            _place.emit(citiesResponse)
        }
    }
}