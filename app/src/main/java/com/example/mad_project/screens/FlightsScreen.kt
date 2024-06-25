package com.example.mad_project.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ViewModel.FlightsViewModel
import com.example.movieappmad24.components.Bars.SimpleTopAppBar
import com.example.mad_project.classes.FlightInfo
import com.example.mad_project.navigation.Screen
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FlightsScreen(
    navController: NavController,
    viewModel: FlightsViewModel,
    jsonString: String
) {
    LaunchedEffect(Unit) {
        val jsonObject = JSONObject(jsonString)
        val origin = jsonObject.getString("startLocation")
        val destination = jsonObject.getString("destination")
        val startDate = jsonObject.getString("startDate")
        val endDate = jsonObject.getString("endDate")

        viewModel.fetchFlights(origin, destination, startDate, endDate)
    }

    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Flights", navController = navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (viewModel.isLoading.value) {
                // Display a loading indicator here
                Text("Loading...")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 56.dp) // Add padding to avoid overlap with the button
                ) {
                    items(viewModel.flights) { flight ->
                        FlightCard(
                            flight = flight,
                            isSelected = viewModel.highlightedFlight.value == flight,
                            onClick = { viewModel.highlightFlight(flight) }
                        )
                    }
                }

                Button(
                    onClick = {
                        val selectedFlight = viewModel.highlightedFlight.value
                        if (selectedFlight != null) {
                            val updatedJsonObject = JSONObject(jsonString).apply {
                                put("selectedFlight", JSONObject().apply {
                                    put("airline", selectedFlight.airline)
                                    put("departure_at", selectedFlight.departure_at)
                                    put("return_at", selectedFlight.return_at)
                                    put("expires_at", selectedFlight.expires_at)
                                    put("price", selectedFlight.price)
                                    put("flight_number", selectedFlight.flight_number)
                                })
                            }
                            val info = updatedJsonObject.toString()
                            navController.navigate(route = Screen.SightsScreen.withJsonString(info))
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Confirm Flight")
                }
            }
        }
    }
}

@Composable
fun FlightCard(flight: FlightInfo, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .background(if (isSelected) Color.Red else Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Airline: ${flight.airline}")
            Text(text = "Departure: ${flight.departure_at}")
            Text(text = "Return: ${flight.return_at}")
            Text(text = "Price: ${flight.price}")
            Text(text = "Flight Number: ${flight.flight_number}")
        }
    }
}
