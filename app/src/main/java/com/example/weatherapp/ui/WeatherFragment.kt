package com.example.weatherapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adapters.ForecastAdapter
import com.example.weatherapp.databinding.BottomSheetLayoutBinding
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private lateinit var sheetlayoutBinding: BottomSheetLayoutBinding
    private lateinit var dialog: BottomSheetDialog
    private val viewModel: WeatherViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        sheetlayoutBinding = DataBindingUtil.inflate(inflater,R.layout.bottom_sheet_layout,container,false)
        dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetTheme)
        dialog.setContentView(sheetlayoutBinding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.weatherforecast.collect{
                binding.data = it.data
            }
        }

        binding.searchView.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_weatherFragment_to_searchFragment)
        }

        binding.forecast.setOnClickListener {
            openDialog(binding.data!!.label,binding.data?.city)
        }
    }

    private fun openDialog(label: String, city: String?) {
        viewModel.getForecast(label,city)
        lifecycleScope.launch {
            viewModel.forecast.collect{
                Log.d("check",it?.data.toString())
                it.data?.let {
                    val adapter = ForecastAdapter(it)
                    sheetlayoutBinding.rvForecast.apply {
                        this.adapter = adapter
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(context,1, RecyclerView.HORIZONTAL,false)
                    }
                }
                dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
                dialog.show()
            }
        }
    }
}