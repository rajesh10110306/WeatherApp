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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel(private val repository: WeatherRepository): ViewModel(){
    private val _weather = MutableStateFlow<Response<LocalWeather>>(Response.Loading())
    val weather: StateFlow<Response<LocalWeather>>
        get() = _weather

    private val _forecast = MutableStateFlow<Response<List<LocalForecast>>>(Response.Loading())
    val forecast: StateFlow<Response<List<LocalForecast>>>
        get() = _forecast

    private val _weatherforecast = MutableStateFlow<Response<LocalForecastWeather>>(Response.Loading())
    val weatherforecast: StateFlow<Response<LocalForecastWeather>>
        get() = _weatherforecast

    private val _place = MutableStateFlow<Response<List<LocalLocation>>>(Response.Loading())
    val place: StateFlow<Response<List<LocalLocation>>>
        get() = _place

    fun getWeather(label: String, city: String?){
        viewModelScope.launch {
            val weatherResponse = repository.getCurrentWeather(label,city)
            _weather.value = weatherResponse
        }
    }

    fun getForecast(label: String, city: String?){
        viewModelScope.launch {
            val forecastResponse = repository.getForecast(label,city)
            _forecast.value = forecastResponse
        }
    }

    fun getForecastWeather(label: String, city: String?){
        viewModelScope.launch {
            try{
                val weather = async { repository.getCurrentWeather(label,city) }
                val forecast = async { repository.getForecast(label,city) }
                if (weather.await().data!=null && forecast.await().data!=null){
                    val weatherForecastResponse = convertToLocalForecastWeather(weather.await().data!!, forecast.await().data!!)
                    _weatherforecast.value = Response.Success(weatherForecastResponse)
                } else{
                    _weatherforecast.value = Response.Failure(weather.await().message+forecast.await().message)
                }
            }
            catch (e: Exception){
                _weatherforecast.value = Response.Failure(e.message.toString())
            }
        }
    }

    fun getCity(pattern: String){
        viewModelScope.launch {
            val citiesResponse = repository.getCity(pattern)
            _place.value = citiesResponse
        }
    }
}