package com.example.weatherapp.repository

import android.content.Context
import android.util.Log
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.data.LocalForecast
import com.example.weatherapp.data.LocalLocation
import com.example.weatherapp.data.LocalWeather
import com.example.weatherapp.db.WeatherDao
import com.example.weatherapp.utils.convertToLocalForecast
import com.example.weatherapp.utils.convertToLocalWeather
import com.example.weatherapp.utils.isNetworkAvailable
import java.lang.Exception

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val placeApi: WeatherApi,
    private val weatherDao: WeatherDao,
    private val applicationContext: Context
): WeatherRepository {
    private val key = "88c0154a25fb21fb0d30003e6956fb4c"
    private val key2 = "fKzVotCk8CmC2teZ3QEn0f9XoL_sZAV585wgTMKFoao"

    override suspend fun getCurrentWeather(label: String, city: String?): Response<LocalWeather>{
        if (isNetworkAvailable(applicationContext)){
            try {
                Log.d("check",label+city)
                val apiData = weatherApi.getCurrentWeather(label,key)
                var weather: LocalWeather? = null
                apiData?.body()?.let {
                    weather = convertToLocalWeather(label,city,it)
                    weatherDao.upsertCityWeather(weather!!)
                }
                if(apiData.code() == 404){
                    val apiData2 = city?.let { weatherApi.getCurrentWeather(it,key) }
                    apiData2?.body()?.let {
                        weather = convertToLocalWeather(label,city,it)
                        weatherDao.upsertCityWeather(weather!!)
                    }
                }
                Log.d("check",apiData.toString())
                return Response.Success(weather)
            }
            catch (e: Exception){
                Log.d("error",e.printStackTrace().toString())
                return Response.Failure(e.message.toString())
            }
        } else{
            return Response.Success(weatherDao.getCityWeather(label),"You are Offline!")
        }
    }

    override suspend fun getForecast(label:String, city:String?): Response<List<LocalForecast>> {
        if (isNetworkAvailable(applicationContext)){
            try {
                val apiData = weatherApi.getForecast(label,key)
                var forecast: List<LocalForecast>? = null
                apiData?.body()?.let {
                    forecast = convertToLocalForecast(it)
                }
                if(apiData.code() == 404){
                    val apiData2 = city?.let { weatherApi.getForecast(it,key) }
                    apiData2?.body()?.let {
                        forecast = convertToLocalForecast(it)
                    }
                }
                Log.d("check",apiData.toString())
                return Response.Success(forecast)
            }
            catch (e: Exception){
                Log.d("error",e.printStackTrace().toString())
                return Response.Failure(e.message.toString())
            }
        }
        else{
            return Response.Success(null,"You are Offline!")
        }
    }

    override suspend fun getCity(pattern:String) : Response<List<LocalLocation>>{
        if (isNetworkAvailable(applicationContext)){
            try {
                Log.d("check",pattern)
                val locationList = ArrayList<LocalLocation>()
                val apiData = placeApi.getCity(pattern,key2)
                apiData?.body()?.let {
                    val items = apiData.body()?.items
                    items?.let {
                        for (i in items){
                            locationList.add(LocalLocation(i.address.label,i.address.city))
                        }
                    }
                }
                Log.d("check",apiData.toString())
                return Response.Success(locationList)
            }
            catch (e: Exception){
                Log.d("error",e.printStackTrace().toString())
                return Response.Failure(e.message.toString())
            }
        } else{
            return Response.Success(weatherDao.getAllCities(),"You are Offline!")
        }
    }
}