package com.example.weatherapp.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun WeatherInfoScreen(navController: NavController,viewModel: WeatherViewModel){
    val weatherInfo by viewModel.weatherforecast.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF0775F7),
                    Color(0xFF03F7F7)
                ),
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY),
                tileMode = TileMode.Clamp
            )
        )
        .padding(30.dp)
    ) {
        SearchCity{
            navController.navigate(Screen.CitySearchScreen.route)
        }
    }
}

@Composable
fun SearchCity(fetchCity: ()->Unit){
    BasicTextField(
        value = "",
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp, CircleShape)
            .background(Color.White, CircleShape)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .clickable {
                fetchCity()
            },
        enabled = false,
        decorationBox = {innerTextField ->
            Text(text = "Search City")
            innerTextField()
        },
    )
}