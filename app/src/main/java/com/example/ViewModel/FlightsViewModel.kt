package com.example.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mad_project.classes.ApiResponse
import com.example.mad_project.classes.Flight
import com.example.mad_project.common.fetchJson
import com.google.gson.Gson

class FlightsViewModel : ViewModel() {

    private val _flights = mutableStateListOf<Flight>()
    val flights: List<Flight>
        get() = _flights

    // Add a loading state
    val isLoading = mutableStateOf(true)

    init {
        Log.i("INIT", "INIT");
        fetchData()
    }

    private fun fetchData() {
        val url = "https://api.travelpayouts.com/v1/prices/direct?origin=VIE&destination&token=8e41eadde108011d6f46c55ceea8a69c"
        fetchJson(url) { jsonData ->
            val gson = Gson()
            val apiResponse = gson.fromJson(jsonData, ApiResponse::class.java)

            // Clear existing flights before adding new ones
            _flights.clear()

            // Flattening the nested structure and adding to _flights
            apiResponse.data.values.forEach { data ->
                _flights.add(data.flight)
            }
            Log.i("APIFATCH" ,jsonData )
            isLoading.value = false
        }
    }
}
