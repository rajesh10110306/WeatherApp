package com.example.weatherapp.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.weatherapp.utils.dateFormatConverter

@BindingAdapter("imageFromUrl")
fun ImageView.imageFromUrl(url: String){
    Glide.with(this.context).load(url).into(this)
}

@BindingAdapter("setDateFormat")
fun TextView.setDateFormat(value: Int){
    this.text = dateFormatConverter(value.toLong())
}