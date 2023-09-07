package com.example.weatherapp.di

import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.repository.WeatherRepositoryImpl
import com.example.weatherapp.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<WeatherRepository> {
        WeatherRepositoryImpl(get(),get(),get(),get())
    }

    viewModel {
        WeatherViewModel(get())
    }
}