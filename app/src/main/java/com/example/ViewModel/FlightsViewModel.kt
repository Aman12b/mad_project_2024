package com.example.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mad_project.classes.ApiResponse
import com.example.mad_project.classes.Feature
import com.example.mad_project.classes.FeatureCollection
import com.example.mad_project.classes.FlightInfo
import com.example.mad_project.common.fetchJson
import com.google.gson.Gson

class FlightsViewModel : ViewModel() {
    private val _flights = mutableStateListOf<FlightInfo>()
    val flights: List<FlightInfo>
        get() = _flights

    // Add a loading state
    val isLoading = mutableStateOf(true)

    init {
        fetchJson("https://api.travelpayouts.com/v1/prices/direct?origin=VIE&destination&token=8e41eadde108011d6f46c55ceea8a69c") { jsonData ->
            val gson = Gson()
            val apiResponse = gson.fromJson(jsonData, ApiResponse::class.java)

            // Flattening the nested structure
            apiResponse.data.values.forEach { flightMap ->
                flightMap.values.forEach { flightInfo ->
                    _flights.add(flightInfo)
                }
            }

            isLoading.value = false
        }
    }
}