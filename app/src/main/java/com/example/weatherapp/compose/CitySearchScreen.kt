package com.example.weatherapp.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatherapp.data.LocalLocation
import com.example.weatherapp.viewmodel.Response
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun CitySearchScreen(navController: NavController,viewModel: WeatherViewModel){
    val cityInfo by viewModel.place.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        SearchBar{
            viewModel.getCity(it)
        }
        when(cityInfo){
            is Response.Loading -> {
                ProgressBar()
            }
            is Response.Success -> {
                cityInfo.data?.let {
                    CityInfo(cityList = it){label,city ->
                        viewModel.getForecastWeather(label,city)
                        navController.navigateUp()
                    }
                }
            }
            is Response.Failure -> {
                ErrorHandler(cityInfo.message)
            }
        }
    }
}

@Composable
private fun SearchBar(fetchCity: (String)->Unit){
    var text by remember{
        mutableStateOf("")
    }
    TextField(
        value = text,
        onValueChange = {
            text = it
            if (it.length>=3)   fetchCity(it)
        },
        label = { Text("Search City") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp),
    )
}

@Composable
private fun CityInfo(cityList: List<LocalLocation>, fetchForecastWeather: (String,String?)->Unit){
    LazyColumn(
    ) {
        items(count = cityList.size) {
            Text(cityList[it].label,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        fetchForecastWeather(cityList[it].label, cityList[it].city)
                    }
                    .padding(horizontal = 30.dp, vertical = 18.dp),
                fontSize = 18.sp
            )
        }
    }
}