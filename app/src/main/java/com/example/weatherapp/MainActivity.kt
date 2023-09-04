package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.WeatherViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var repository: WeatherRepository
    private lateinit var factory: WeatherViewModelFactory
    private lateinit var viewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = (application as WeatherApplication).weatherRepository
        factory = WeatherViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(WeatherViewModel::class.java)
    }
}