package com.example.weatherapp.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.utils.dateFormatConverter
import com.example.weatherapp.viewmodel.Response
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlin.math.ceil

private val climateType = listOf("Sunrise", "Sunset", "Wind Speed", "Pressure", "Humidity", "Air Quality")
private val climateImage = listOf(R.drawable.sunrise,R.drawable.sunset,R.drawable.wind,R.drawable.pressure,R.drawable.humidity,R.drawable.smoke)
private val forecastType = listOf("Avg Temp","Avg Pressure","Avg Humidity")

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
        when(weatherInfo){
            is Response.Loading -> {
                ProgressBar()
            }
            is Response.Success -> {
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
                    ClimateAnalysis(listOf(dateFormatConverter(it.sunrise.toLong()),
                        dateFormatConverter(it.sunset.toLong()),
                        it.speed.toString() + " Kmph",
                        it.pressure.toString() + " hPa",
                        it.humidity.toString() + " %",
                        "perfect"
                    ))
                    ForecastAnalysis(listOf(it.avgTemp.toString() + " °C",
                        it.avgPressure.toString() + " hPa",
                        it.avgHumidity.toString() + " %"
                    ))
                }
            }
            is Response.Failure -> {
                ErrorHandler(weatherInfo.message)
            }
        }
    }
}

@Composable
private fun SearchCity(fetchCity: ()->Unit){
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
private fun LocationInfo(label: String, updateTime: String) {
    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddH1Text(text = label)
        AddH3Text(text = updateTime)
    }
}

@Composable
private fun WeatherInfo(
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
private fun ClimateAnalysis(climateValues: List<String>) {
    Box(modifier = Modifier
        .padding(top = 20.dp)
        .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f),
            colors = CardDefaults.cardColors(Color(0xFF69CDFA))
        ){
            Column(modifier = Modifier
                .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 1..ceil(climateType.size.toFloat()/3).toInt()){
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (j in 0..2){
                            Column(horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = climateImage[(i-1)*3+j]),
                                    contentDescription = "sunrise Img",
                                    modifier = Modifier
                                        .width(32.dp)
                                        .height(32.dp)
                                        .padding(vertical = 5.dp)
                                )
                                AddH2Text(text = climateType[(i-1)*3+j])
                                AddH3Text(text = climateValues[(i-1)*3+j])
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun ForecastAnalysis(forecastValues: List<String>) {
    Row(modifier = Modifier
        .padding(top = 20.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in forecastType.indices){
            Column(horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AddH2Text(text = forecastType[i])
                AddH3Text(text = forecastValues[i])
            }
        }
    }
}

@Composable
private fun AddH1Text(text: String){
    Text(text = text, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 5.dp), fontSize = 32.sp)
}

@Composable
private fun AddH2Text(text: String){
    Text(text = text, modifier = Modifier.padding(vertical = 5.dp), fontSize = 20.sp)
}

@Composable
private fun AddH3Text(text: String){
    Text(text = text, modifier = Modifier.padding(vertical = 5.dp), fontSize = 16.sp)
}