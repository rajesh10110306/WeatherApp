package com.example.weatherapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class Location(private val context: Context){
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    fun getCurrentLocation(viewModel: WeatherViewModel) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener(context as Activity){ task ->
            val location = task.result
            val geocoder = Geocoder(context, Locale.getDefault())
            var city: String = "London"
            if(location==null){
                Toast.makeText(context,"Null Received", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"Get Success", Toast.LENGTH_SHORT).show()
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
                    geocoder.getFromLocation(location.latitude,location.longitude,1,object: Geocoder.GeocodeListener{
                        override fun onGeocode(address: MutableList<Address>) {
                            city = address[0].locality
                        }
                    })
                }else{
                    city = geocoder.getFromLocation(location.latitude,location.longitude,1)?.get(0)?.locality.toString()
                }
//                viewModel.getLocationWeather(location.latitude.toString(),location.longitude.toString())
                viewModel.getForecastWeather("Current Location",city)
            }
        }
    }
}