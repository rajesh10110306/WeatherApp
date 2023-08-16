package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.data.LocalLocation
import com.example.weatherapp.model.data.LocalWeather
import com.example.weatherapp.model.interfaces.RetrofitHelper
import com.example.weatherapp.model.interfaces.RetrofitHelper2
import com.example.weatherapp.model.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel(private val repository: WeatherRepository): ViewModel() {
    private lateinit var result : LocalWeather
    val data = MutableLiveData<LocalWeather>()
    private var result2:ArrayList<LocalLocation> = ArrayList()
    val data2 = MutableLiveData<ArrayList<LocalLocation>>()
    private lateinit var error: String
    val exceptionError = MutableLiveData<String>()
    var isOnline:Boolean = true

    init {
        getData("Dummy","London")
    }

    fun getCurrentData(lat:String,lon:String){
        val key = "88c0154a25fb21fb0d30003e6956fb4c"
        var endPoint = "data/2.5/weather?lat=$lat&lon=$lon&appid=$key"
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val apiData = RetrofitHelper.api.getCurrentWeather(endPoint)
                apiData?.body()?.let {
                    result = LocalWeather(
                        "Current Location",
                        it.name,
                        it.main.temp,
                        it.main.temp_min,
                        it.main.temp_max,
                        it.main.humidity,
                        it.main.feels_like,
                    )
                    Log.d("API Response Weather", result.toString())
                    data.postValue(result)
                    repository.upsertCityWeather(result)
                }
            } catch (e: Exception) {
                error = e.toString()
                exceptionError.postValue(error)
            }
        }
    }

    fun getData(label: String, city: String?) {
        if (isOnline){
            getDataFromApi(label,city)
        }else{
            getDataFromRoom(label,city)
        }
    }

    fun getDataFromApi(label:String,city: String?){
        Log.d("API Call",label)
        Log.d("API Call",city?:"")
        val key = "88c0154a25fb21fb0d30003e6956fb4c"
        var endPoint = "data/2.5/weather?q=$label&appid=$key"
        var endPoint2 = "data/2.5/weather?q=$city&appid=$key"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val apiData = RetrofitHelper.api.getCurrentWeather(endPoint)
                apiData?.body()?.let {
                    result = LocalWeather(
                        label,
                        city,
                        it.main.temp,
                        it.main.temp_min,
                        it.main.temp_max,
                        it.main.humidity,
                        it.main.feels_like,
                    )
                    Log.d("API Response Weather", result.toString())
                    data.postValue(result)
                    repository.upsertCityWeather(result)
                }
                if(apiData.code() == 404){
                    val apiData2 = RetrofitHelper.api.getCurrentWeather(endPoint2)
                    apiData2?.body()?.let {
                        result = LocalWeather(
                            label,
                            city,
                            it.main.temp,
                            it.main.temp_min,
                            it.main.temp_max,
                            it.main.humidity,
                            it.main.feels_like,
                        )
                        Log.d("API Response Weather", result.toString())
                        data.postValue(result)
                        repository.upsertCityWeather(result)
                    }
                }
            } catch (e: Exception) {
                error = e.toString()
                exceptionError.postValue(error)
            }
        }
    }
    fun getDataFromRoom(label:String,city: String?){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                result = repository.getCityWeather(label)
                data.postValue(result)
            } catch (e: Exception) {
                error = e.toString()
                exceptionError.postValue(error)
            }
        }
    }

    fun getCityList(pattern:String){
        if (isOnline){
            getCityListFromApi(pattern)
        }else{
            getCityListFromRoom()
        }
    }

    fun getCityListFromApi(pattern:String){
        val key = "fKzVotCk8CmC2teZ3QEn0f9XoL_sZAV585wgTMKFoao"
        var endPoint = "autocomplete?q=$pattern&apiKey=$key"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val apiData = RetrofitHelper2.api.getCity(endPoint)
                apiData?.body()?.let {
                    val items = apiData.body()?.items
                    items?.let {
                        val labels:ArrayList<LocalLocation> = ArrayList()
                        for (i in items){
                            labels.add(LocalLocation(i.address.label,i.address.city))
                        }
                        result2 = labels
                        Log.d("API Response",result2.toString())
                        data2.postValue(result2)
                    }
                }
            } catch (e: Exception) {
                error = e.toString()
                exceptionError.postValue(error)
            }
        }
    }
    fun getCityListFromRoom(){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                result2 = repository.getAllCities() as ArrayList<LocalLocation>
                data2.postValue(result2)
            }catch (e: Exception) {
                error = e.toString()
                exceptionError.postValue(error)
            }
        }
    }
}