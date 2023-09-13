package com.example.weatherapp.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun CitySearchScreen(navController: NavController,viewModel: WeatherViewModel){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        SearchBar{
            viewModel.getCity(it)
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