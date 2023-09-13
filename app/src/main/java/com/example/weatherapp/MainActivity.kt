package com.example.weatherapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.weatherapp.compose.Navigation
import com.example.weatherapp.utils.UserPermission
import com.example.weatherapp.utils.getCurrentLocation
import com.example.weatherapp.utils.requestPermission
import com.example.weatherapp.utils.checkPermission
import com.example.weatherapp.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<WeatherViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation(viewModel)
        }

        if(!checkPermission()){
            requestPermission()
        }
        getCurrentLocation{city ->
            viewModel.getForecastWeather("Current Location",city)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == UserPermission.PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation{city ->
                    viewModel.getForecastWeather("Current Location",city)
                }
            }
            else{
                Toast.makeText(this,"Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}