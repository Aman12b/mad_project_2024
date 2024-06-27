package com.example.ViewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mad_project.classes.ApiResponse
import com.example.mad_project.classes.FlightData
import com.example.mad_project.common.fetchJson
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FlightsViewModel : ViewModel() {
    private val _flights = mutableStateListOf<FlightData>()
    val flights: List<FlightData>
        get() = _flights

    // Add a loading state
    val isLoading = mutableStateOf(true)
    val isError = mutableStateOf("")
    val highlightedFlight = mutableStateOf<FlightData?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchFlights(origin: String, destination: String, startDate: String, endDate: String) {
        isLoading.value = true

        // Extract IATA code
        val originIata = extractIataCode(origin)
        val destinationIata = extractIataCode(destination)

        // Reformat dates
        val formattedStartDate = reformatDate(startDate)
        val formattedEndDate = reformatDate(endDate)

        val token = "8e41eadde108011d6f46c55ceea8a69c"
        val apiUrl = "https://api.travelpayouts.com/v1/prices/calendar?" +
                "origin=$originIata" +
                "&destination=$destinationIata" +
                "&depart_date=$formattedStartDate" +
                "&return_date=$formattedEndDate"+
                "&currency=eur" +
                "&token=$token";
        fetchJson(apiUrl) { jsonData, errorMessage ->
            if (errorMessage != null) {
                isError.value = "error fetching data";
                Log.e("API FETCH ERROR", errorMessage)
                _flights.clear()
                isLoading.value = false
            } else {
                val gson = Gson()
                val apiResponse = gson.fromJson(jsonData, ApiResponse::class.java)

                // Flattening the nested structure
                _flights.clear()
                apiResponse.data.values.forEach { flightData ->
                    _flights.add(flightData)
                }

                if (_flights.isEmpty()) {
                    isError.value = "No flights found"
                    Log.e("API FETCH ERROR", "No flights found")
                }

                isLoading.value = false
            }
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

    fun highlightFlight(flight: FlightData) {
        highlightedFlight.value = flight
    }
}