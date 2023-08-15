package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.data.LocalLocation
import com.example.weatherapp.model.interfaces.WeatherDatabase
import com.example.weatherapp.model.repository.WeatherRepository
import com.example.weatherapp.view.adapters.RecyclerAdapter
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.WeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: WeatherViewModel
    private lateinit var weatherDatabase: WeatherDatabase
    private lateinit var repository: WeatherRepository
    private lateinit var factory: WeatherViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        weatherDatabase = WeatherDatabase(this)
        repository = WeatherRepository(weatherDatabase)
        factory = WeatherViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(WeatherViewModel::class.java)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermissions()
        getCurrentLocation()

        viewModel.data.observe(this, Observer {
            binding.temp.text = it.temp.toString() +" \u2109"
            binding.tempDiff.text = it.temp_min.toString() + "  ~  " + it.temp_max.toString() +" \u2109"
            binding.humidity.text = "Humidity " + it.humidity.toString() + " %"
            binding.feelsLike.text = "Feels like - " + it.feels_like.toString() +" \u2109"
            binding.cityName.text = it.label
        })

        viewModel.data2.observe(this, Observer {
            prepareRecyclerView(binding,viewModel,it)
        })

        viewModel.exceptionError.observe(this, Observer {
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        })

        binding.editText.setOnClickListener{
            binding.searchView.visibility = View.VISIBLE
            binding.cardView.visibility = View.INVISIBLE
        }

        binding.editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    if(p0.toString().length>=3){
                        viewModel.isOnline = isOnline(applicationContext)
                        if (!viewModel.isOnline)    Toast.makeText(this@MainActivity,"You are Offline!!",Toast.LENGTH_SHORT).show()
                        viewModel.getCityList(p0.toString())
                    }
                }
            }

        })

        if (!isOnline(applicationContext)){
            viewModel.isOnline = false
            viewModel.getData("Current Location","")
        }
    }

    fun prepareRecyclerView(binding:ActivityMainBinding,viewModel: WeatherViewModel, data: ArrayList<LocalLocation>){
        this.binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerAdapter(binding,viewModel,this,data)
        this.binding.recyclerView.adapter = adapter
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private fun getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("check","Not yet Started")
            return
        }
        Log.d("check","Started")
        fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task ->
            val location = task.result
            if(location==null){
                Toast.makeText(this,"Null Received", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Get Success", Toast.LENGTH_SHORT).show()
                viewModel.getCurrentData(location.latitude.toString(),location.longitude.toString())
            }
        }
    }

    private fun checkLocationPermissions(){
        if(!isLocationEnabled()){
            Toast.makeText(this,"Turn on Location", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        if(!checkPermissions()) requestPermission()
    }

    private fun isLocationEnabled():Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isLocationEnabled
    }


    private fun checkPermissions():Boolean{
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 200
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }
            else{
                Toast.makeText(this,"Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}