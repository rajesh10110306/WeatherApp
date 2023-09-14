package com.example.weatherapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.LocalForecast
import com.example.weatherapp.data.LocalForecastWeather
import com.example.weatherapp.data.LocalLocation
import com.example.weatherapp.data.LocalWeather
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.convertToLocalForecast
import com.example.weatherapp.utils.convertToLocalForecastWeather
import com.example.weatherapp.utils.convertToLocalWeather
import com.example.weatherapp.utils.isNetworkAvailable
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.Exception

class WeatherViewModel(private val repository: WeatherRepository, private val applicationContext: Context): ViewModel(){
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
        _weather.value = Response.Loading()
        if (isNetworkAvailable(applicationContext))
            getWeatherFromApi(label, city)
        else
            getWeatherFromRoom(label)
    }

    private fun getWeatherFromApi(label: String, city: String?){
        try{
            viewModelScope.launch {
                val apiData = repository.getWeatherFromApi(label)
                when{
                    apiData.code()!=404 -> {
                        apiData?.body()?.let {
                            val weather = convertToLocalWeather(label,city,it)
                            _weather.value = Response.Success(weather)
                            repository.UpdateWeatherInRoom(weather)
                        }
                    }
                    (apiData.code() == 404 && !city.isNullOrEmpty()) -> {
                        val apiData = repository.getWeatherFromApi(city)
                        if(apiData.code()==404){
                            _forecast.value = Response.Failure("Error 404.\nSomething Went Wrong.\nPlease try again later")
                            return@launch
                        }
                        apiData?.body()?.let {
                            val weather = convertToLocalWeather(label,city,it)
                            _weather.value = Response.Success(weather)
                            repository.UpdateWeatherInRoom(weather)
                        }
                    }
                    else -> {
                        _weather.value = Response.Failure("Something Went Wrong.\nPlease try again later")
                    }
                }
            }
        }
        catch (e: Exception){
            Log.d("error",e.printStackTrace().toString())
            _weather.value = Response.Failure(e.toString())
        }
    }

    private fun getWeatherFromRoom(label: String){
        try {
            viewModelScope.launch {
                _weather.value = Response.Success(repository.getWeatherFromRoom(label),"You are Offline!")
            }
        }
        catch (e: Exception){
            Log.d("error",e.printStackTrace().toString())
            _weather.value = Response.Failure(e.toString())
        }
    }

    fun getForecast(label: String, city: String?){
        _forecast.value = Response.Loading()
        if (isNetworkAvailable(applicationContext))
            getForecastFromApi(label, city)
        else
            _forecast.value = Response.Failure("Your are Offline!")
    }

    private fun getForecastFromApi(label: String, city: String?){
        try{
            viewModelScope.launch {
                val apiData = repository.getForecastFromApi(label)
                when{
                    apiData.code()!=404 -> {
                        apiData?.body()?.let {
                            val forecast = convertToLocalForecast(it)
                            _forecast.value = Response.Success(forecast)
                        }
                    }
                    (apiData.code() == 404 && !city.isNullOrEmpty()) -> {
                        val apiData = repository.getForecastFromApi(city)
                        if(apiData.code()==404){
                            _forecast.value = Response.Failure("Error 404.\nSomething Went Wrong.\nPlease try again later")
                            return@launch
                        }
                        apiData?.body()?.let {
                            val forecast = convertToLocalForecast(it)
                            _forecast.value = Response.Success(forecast)
                        }
                    }
                    else -> {
                        _forecast.value = Response.Failure("Something Went Wrong.\nPlease try again later")
                    }
                }
            }
        }
        catch (e: Exception){
            Log.d("error",e.printStackTrace().toString())
            _forecast.value = Response.Failure(e.toString())
        }
    }

    fun getForecastWeather(label: String, city: String?){
        _weatherforecast.value = Response.Loading()
        if (isNetworkAvailable(applicationContext))
            getForecastWeatherFromApi(label,city)
        else
            _weatherforecast.value = Response.Failure("Your are Offline!")
    }

    private fun getForecastWeatherFromApi(label: String, city: String?){
        try {
            viewModelScope.launch {
                val weatherData = async { repository.getWeatherFromApi(label) }
                val forecastData = async { repository.getForecastFromApi(label) }
                when{
                    weatherData.await().code()!=404 && forecastData.await().code()!=404  -> {
                        weatherData.await().body()?.let {weather->
                            forecastData.await().body()?.let {forecast->
                                _weatherforecast.value = Response.Success(
                                    convertToLocalForecastWeather(convertToLocalWeather(label,city,weather),
                                        convertToLocalForecast(forecast))
                                )
                            }
                        }
                        Log.d("check",weatherData.await().toString())
                    }
                    (city!=null) -> {
                        val weatherData = async { repository.getWeatherFromApi(city) }
                        val forecastData = async { repository.getForecastFromApi(city) }
                        if(weatherData.await().code()==404){
                            _weatherforecast.value = Response.Failure("Error 404.\nSomething Went Wrong.\nPlease try again later")
                            return@launch
                        }
                        weatherData.await().body()?.let {weather->
                            forecastData.await().body()?.let {forecast->
                                _weatherforecast.value = Response.Success(
                                    convertToLocalForecastWeather(convertToLocalWeather(label,city,weather),
                                        convertToLocalForecast(forecast))
                                )
                            }
                        }
                    }
                    else -> {
                        _weatherforecast.value = Response.Failure("Something Went Wrong.\nPlease try again later")
                    }
                }
            }
        }
        catch (e: Exception){
            Log.d("error",e.printStackTrace().toString())
            _weatherforecast.value = Response.Failure(e.toString())
        }
    }

    fun getCity(pattern: String){
        _place.value = Response.Loading()
        if(isNetworkAvailable(applicationContext))
            getCitiesFromApi(pattern)
        else
            getCitiesFromRoom(pattern)
    }

    private fun getCitiesFromApi(pattern: String){
        try{
            viewModelScope.launch {
                val apiData = repository.getCitiesFromApi(pattern)
                if(apiData.code()==404){
                    _place.value = Response.Failure("Something Went Wrong.\nPlease try again later")
                    return@launch
                }
                apiData?.body()?.let {
                    val items = apiData.body()?.items
                    items?.let {
                        val locationList = ArrayList<LocalLocation>()
                        for (i in items) {
                            locationList.add(LocalLocation(i.address.label, i.address.city))
                        }
                        _place.value = Response.Success(locationList)
                    }
                }
            }
        }
        catch (e: Exception){
            Log.d("error",e.printStackTrace().toString())
            _place.value = Response.Failure(e.toString())
        }
    }

    private fun getCitiesFromRoom(pattern: String){
        try {
            viewModelScope.launch {
                _place.value = Response.Success(repository.getCitiesFromRoom(),"You are Offline!")
            }
        }
        catch (e: Exception){
            Log.d("error",e.printStackTrace().toString())
            _place.value = Response.Failure(e.toString())
        }
    }
}