package com.example.mad_project.classes

import com.google.gson.annotations.SerializedName

data class Flight(
    @SerializedName("airline") val airline: String,
    @SerializedName("departure_at") val departureAt: String,
    @SerializedName("return_at") val returnAt: String,
    @SerializedName("expires_at") val expiresAt: String,
    @SerializedName("price") val price: Int,
    @SerializedName("flight_number") val flightNumber: Int
)

data class Data(
    @SerializedName("0") val flight: Flight
)

data class ApiResponse(
    @SerializedName("data") val data: Map<String, Data>,
    @SerializedName("currency") val currency: String,
    @SerializedName("success") val success: Boolean
)
