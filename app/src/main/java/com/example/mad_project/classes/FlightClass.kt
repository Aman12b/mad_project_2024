package com.example.mad_project.classes

import androidx.compose.runtime.mutableStateListOf

data class ApiResponse(
    val data: Map<String, Map<String, FlightInfo>>,
    val currency: String,
    val success: Boolean
)

data class FlightInfo(
    val airline: String,
    val departure_at: String,
    val return_at: String,
    val expires_at: String,
    val price: Int,
    val flight_number: Int
)