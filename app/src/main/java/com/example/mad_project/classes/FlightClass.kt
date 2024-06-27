package com.example.mad_project.classes

import androidx.compose.runtime.mutableStateListOf

data class FlightData(
    val origin: String,
    val destination: String,
    val airline: String,
    val departure_at: String,
    val return_at: String,
    val expires_at: String,
    val price: Int,
    val flight_number: Int,
    val transfers: Int
)

data class ApiResponse(
    val data: Map<String, FlightData>,
    val currency: String,
    val success: Boolean
)