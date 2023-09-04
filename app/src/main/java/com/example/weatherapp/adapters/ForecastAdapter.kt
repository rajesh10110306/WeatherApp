package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.LocalForecast
import com.example.weatherapp.databinding.ForecastRecyclerItemBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ForecastAdapter( private val forecastArray: List<LocalForecast>) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    class ViewHolder(val binding : ForecastRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ForecastRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = forecastArray.get(position)
        holder.binding.apply {
            imgItem.imageFromUrl("https://openweathermap.org/img/w/${currentItem.icon}.png")
            tvItemTemp.text = "${currentItem.temp.toInt()} °C"
            tvItemStatus.text = "${currentItem.description}"
            tvItemTime.text = displayTime(currentItem.dt_txt)

        }
    }

    private fun displayTime(dtTxt: String): CharSequence? {
        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val output = DateTimeFormatter.ofPattern("MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(dtTxt,input)
        return output.format(dateTime)

    }

    override fun getItemCount(): Int {
        return forecastArray.size
    }
}