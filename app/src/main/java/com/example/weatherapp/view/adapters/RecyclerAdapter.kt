package com.example.weatherapp.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.data.LocalCity
import com.example.weatherapp.model.data.Location
import com.example.weatherapp.viewmodel.WeatherViewModel

class RecyclerAdapter(private val viewModel: WeatherViewModel, private val context: Context, private val city: ArrayList<LocalCity>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return city.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName.text = city[position].city+", "+city[position].state+", "+city[position].country
        holder.item.setOnClickListener {
            Log.d("check","Btn clicked")
            viewModel.getData(Location(city[position].lat,city[position].lon,city[position].city,city[position].state,city[position].country))
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val item = itemView.findViewById<CardView>(R.id.item)
        val cityName = itemView.findViewById<TextView>(R.id.cityName)
    }
}