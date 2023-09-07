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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale
fun Context.getCurrentLocation(getLocalWeather: (city: String)->Unit) {
    val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return
    }
    fusedLocationProviderClient.lastLocation.addOnCompleteListener(this as Activity){ task ->
        val location = task.result
        val geocoder = Geocoder(this, Locale.getDefault())
        var city = "London"
        if(location==null){
            Toast.makeText(this,"Null Received", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this,"Get Success", Toast.LENGTH_SHORT).show()
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
                geocoder.getFromLocation(location.latitude,location.longitude,1,object: Geocoder.GeocodeListener{
                    override fun onGeocode(address: MutableList<Address>) {
                        city = address[0].locality
                    }
                })
            }else{
                city = geocoder.getFromLocation(location.latitude,location.longitude,1)?.get(0)?.locality.toString()
            }
            getLocalWeather(city)
        }
    }
}