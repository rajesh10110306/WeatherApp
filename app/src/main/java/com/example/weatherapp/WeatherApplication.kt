package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.di.apiModule
import com.example.weatherapp.di.appModule
import com.example.weatherapp.di.databaseModule
import com.example.weatherapp.di.repositoryModule
import com.example.weatherapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeatherApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WeatherApplication)
            modules(appModule, apiModule, databaseModule, repositoryModule, viewModelModule)
        }
    }
}