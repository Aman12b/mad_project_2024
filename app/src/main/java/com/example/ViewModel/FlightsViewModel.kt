package com.example.ViewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mad_project.classes.ApiResponse
import com.example.mad_project.classes.FlightInfo
import com.example.mad_project.common.fetchJson
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FlightsViewModel : ViewModel() {
    private val _flights = mutableStateListOf<FlightInfo>()
    val flights: List<FlightInfo>
        get() = _flights

    // Add a loading state
    val isLoading = mutableStateOf(true)

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchFlights(origin: String, destination: String, startDate: String, endDate: String) {
        isLoading.value = true

        // Extract IATA code
        val originIata = extractIataCode(origin)
        val destinationIata = extractIataCode(destination)

        // Reformat dates
        val formattedStartDate = reformatDate(startDate)
        val formattedEndDate = reformatDate(endDate)

        val apiUrl = "https://api.travelpayouts.com/v1/prices/cheap?origin=$originIata&destination=$destinationIata&departure_at=$formattedStartDate&return_at=$formattedEndDate&currency=eur&token=8e41eadde108011d6f46c55ceea8a69c"
        fetchJson(apiUrl) { jsonData ->
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

    private fun extractIataCode(location: String): String {
        return location.substringAfter("(", "").substringBefore(")")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun reformatDate(date: String): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val parsedDate = LocalDate.parse(date, formatter)
        return parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}
