package com.example.weatherapp.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.weatherapp.utils.dateFormatConverter
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
        weatherInfo.data?.let {
            LocationInfo(it.label,
                dateFormatConverter(it.update_time.toLong())
            )
            WeatherInfo(it.status,
                it.icon,
                it.temp.toString() + " °C",
                "Feels Like " + it.feels_like.toString() + " °C",
                "Min Temp " + it.temp_min.toString() + " °C",
                "Max Temp " + it.temp_max.toString() + " °C"
            )
        }?:Spacer(modifier = Modifier.height(20.dp))
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

@Composable
fun LocationInfo(label: String, updateTime: String) {
    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddH1Text(text = label)
        AddH3Text(text = updateTime)
    }
}

@Composable
fun WeatherInfo(
    status: String,
    icon: String,
    temp: String,
    feelsLike: String,
    tempMin: String,
    tempMax: String
) {
    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddH2Text(text = status)
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AddH1Text(text = temp)
            AsyncImage(
                model = "https://openweathermap.org/img/wn/$icon@2x.png",
                contentDescription = null,
                modifier = Modifier
                    .height(128.dp)
                    .width(128.dp)
            )
        }
        AddH3Text(text = feelsLike)
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AddH3Text(text = tempMin)
            AddH3Text(text = tempMax)
        }
    }
}

@Composable
fun AddH1Text(text: String){
    Text(text = text, modifier = Modifier.padding(vertical = 5.dp), fontSize = 32.sp)
}

@Composable
fun AddH2Text(text: String){
    Text(text = text, modifier = Modifier.padding(vertical = 5.dp), fontSize = 20.sp)
}

@Composable
fun AddH3Text(text: String){
    Text(text = text, modifier = Modifier.padding(vertical = 5.dp), fontSize = 16.sp)
}