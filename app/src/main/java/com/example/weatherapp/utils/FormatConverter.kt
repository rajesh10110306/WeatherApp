package com.example.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun dateFormatConverter(date: Long): String {
    return SimpleDateFormat(
        "hh:mm a",
        Locale.ENGLISH
    ).format(Date(date * 1000))
}