package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.LocalForecast
import com.example.weatherapp.data.LocalForecastWeather
import com.example.weatherapp.data.LocalLocation
import com.example.weatherapp.data.LocalWeather
import com.example.weatherapp.repository.Response
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.convertToLocalForecastWeather
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel(private val repository: WeatherRepository): ViewModel(){
    private val _weather = MutableSharedFlow<Response<LocalWeather>>(1)
    val weather: SharedFlow<Response<LocalWeather>>
        get() = _weather

    private val _forecast = MutableSharedFlow<Response<List<LocalForecast>>>(1)
    val forecast: SharedFlow<Response<List<LocalForecast>>>
        get() = _forecast

    private val _weatherforecast = MutableSharedFlow<Response<LocalForecastWeather>>(1)
    val weatherforecast: SharedFlow<Response<LocalForecastWeather>>
        get() = _weatherforecast

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

    fun getForecastWeather(label: String, city: String?){
        viewModelScope.launch {
            try{
                val weather = async { repository.getCurrentWeather(label,city) }
                val forecast = async { repository.getForecast(label,city) }
                val weatherForecastResponse = convertToLocalForecastWeather(weather.await().data!!, forecast.await().data!!)
                _weatherforecast.emit(Response.Success(weatherForecastResponse))
            }
            catch (e: Exception){
                _weatherforecast.emit(Response.Failure(e.message.toString()))
            }
        }
    }

    fun getCity(pattern: String){
        viewModelScope.launch {
            val citiesResponse = repository.getCity(pattern)
            _place.emit(citiesResponse)
        }
    }
}