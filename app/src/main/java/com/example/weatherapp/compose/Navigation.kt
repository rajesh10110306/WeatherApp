package com.example.weatherapp.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun Navigation(viewModel: WeatherViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.WeatherInfoScreen.route){
        composable(
            route = Screen.WeatherInfoScreen.route
        ){
            WeatherInfoScreen(navController = navController,viewModel)
        }
        composable(
            route = Screen.CitySearchScreen.route
        ){
            CitySearchScreen(navController = navController,viewModel)
        }
    }
}