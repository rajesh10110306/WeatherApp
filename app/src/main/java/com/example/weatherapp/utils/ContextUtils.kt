package com.example.weatherapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weatherapp.utils.UserPermission.Companion.PERMISSION_REQUEST_ACCESS_LOCATION
import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class UserPermission{
    companion object{
        val PERMISSION_REQUEST_ACCESS_LOCATION = 200
    }
}

private fun isLocationEnabled(context: Context):Boolean {
    return when{
        Build.VERSION.SDK_INT>= Build.VERSION_CODES.P -> {
            val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.isLocationEnabled
        }
        else -> {
            val mode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF)
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }
}

private fun checkLocationPermissions(context: Context):Boolean{
    return (ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
}

fun Context.checkPermission(): Boolean{
    return (isLocationEnabled(this) && checkLocationPermissions(this))
}

fun Context.requestPermission(){
    if (!isLocationEnabled(this)){
        Toast.makeText(this,"Turn on Location", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        this.startActivity(intent)
    }
    if (!checkLocationPermissions(this)){
        when(this){
            is Activity -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_ACCESS_LOCATION
                )
            }
            else -> {
                Log.e("User Permission", "Context can't be smart cast to Activity")
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun Context.getCurrentLocation(getLocalWeather: (city: String)->Unit) {
    when{
        !checkLocationPermissions(this) -> return
        this is Activity -> {
            val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task ->
                val location = task.result
                val geocoder = Geocoder(this, Locale.getDefault())
                if(location==null){
                    Toast.makeText(this,"Null Received", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"Get Success", Toast.LENGTH_SHORT).show()
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
                        geocoder.getFromLocation(location.latitude,location.longitude,1,object: Geocoder.GeocodeListener{
                            override fun onGeocode(address: MutableList<Address>) {
                                getLocalWeather(address[0].locality)
                            }
                        })
                    }else{
                        getLocalWeather(geocoder.getFromLocation(location.latitude,location.longitude,1)?.get(0)?.locality.toString())
                    }
                }
            }
        }
        else -> {
            Log.e("User Permission", "Context can't be smart cast to Activity")
        }
    }
}