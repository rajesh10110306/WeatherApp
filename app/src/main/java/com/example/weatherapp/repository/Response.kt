package com.example.weatherapp.repository

sealed class Response<T>(val data: T?=null, val message: String?=null){
    class Loading<T>: Response<T>()
    class Success<T>(data: T?=null, message: String?=null): Response<T>(data = data, message = message)
    class Failure<T>(message: String?=null): Response<T>(message = message)
}