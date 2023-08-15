package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.data.LocalCity
import com.example.weatherapp.model.data.LocalWeather
import com.example.weatherapp.model.data.Location
import com.example.weatherapp.model.interfaces.RetrofitHelper
import com.example.weatherapp.model.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel(private val repository: WeatherRepository): ViewModel() {
    private lateinit var result : LocalWeather
    val data = MutableLiveData<LocalWeather>()
    private lateinit var result2 : ArrayList<LocalCity>
    val data2 = MutableLiveData<ArrayList<LocalCity>>()
    private lateinit var error: String
    val exceptionError = MutableLiveData<String>()
    var location = Location(0.0,0.0,"","","")

    init {
        getData()
    }

    fun getData(location: Location=this.location) {
        GlobalScope.launch (Dispatchers.IO) {
            val flag = getDataFromRoom(location)
            if(!flag) getDataFromApi(location)
        }
    }

    fun getDataFromApi(location:Location){
        val key = "88c0154a25fb21fb0d30003e6956fb4c"
        var endPoint = "data/2.5/weather?lat=${location.lat}&lon=${location.lon}&appid=$key"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val apiData = RetrofitHelper.api.getCurrentWeather(endPoint)
                apiData?.body()?.let {
                    result = LocalWeather(it.main.temp,it.main.temp_min,it.main.temp_max,it.main.humidity,it.main.feels_like,location.lat,location.lon,location.city,location.state,location.country)
                    Log.d("API Response Weather",result.toString())
                    data.postValue(result)
                    repository.insertCityWeather(result)
                }
            }
            catch (e:Exception){
                error = "Not Able to Fetch! \n Check Your Internet Connection"
                exceptionError.postValue(error)
            }
        }
    }

    suspend fun getDataFromRoom(location:Location):Boolean{
        val roomData = GlobalScope.async (Dispatchers.IO){
            Log.d("Room call Check",location.toString())
            repository.getCityWeather(location.lat,location.lon)
        }
        roomData.await()?.let {
            data.postValue(it)
            return true
        }
        roomData.await().let {
            return false
        }
    }

    fun getCityList(city: String){
        GlobalScope.launch (Dispatchers.IO) {
            val flag = getCityFromRoom(city)
            if(!flag) getCityFromApi(city)
        }
    }

    suspend fun getCityFromRoom(city: String):Boolean{
        val roomData = GlobalScope.async (Dispatchers.IO){
            Log.d("Room call Check",city)
            repository.getCity(city)
        }
        roomData.await()?.let {
            result2 = it as ArrayList<LocalCity>
            data2.postValue(result2)
        }
        if(roomData.await().isNotEmpty())    return true
        return false
    }

    suspend fun getCityFromApi(city: String){
        val endPoint = "geo/1.0/direct?q=$city&limit=5&appid=88c0154a25fb21fb0d30003e6956fb4c"
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val apiData = RetrofitHelper.api.getCityList(endPoint)
                apiData?.body()?.let {
                    val localCity = ArrayList<LocalCity>()
                    for(i in it){
                        localCity.add(LocalCity(i.lat,i.lon,i.name,i.state,i.country))
                    }
                    result2 = localCity
                    Log.d("API Response City",result2.toString())
                    data2.postValue(result2)
                    repository.insertCity(result2)
                }
            }
            catch (e:Exception){
                error = "Not Able to Fetch! \n Check Your Internet Connection"
                exceptionError.postValue(error)
            }
        }
    }
}