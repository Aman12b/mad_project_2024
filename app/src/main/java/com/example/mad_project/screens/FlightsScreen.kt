package com.example.mad_project.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ViewModel.FlightsViewModel
import com.example.mad_project.classes.Flight
import com.example.movieappmad24.components.Bars.SimpleTopAppBar

@Composable
fun FlightsScreen(
    navController: NavController,
    viewModel: FlightsViewModel
) {

    val isLoading by remember { viewModel.isLoading }

    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Flights", navController = navController)
        }
    ) { innerPadding ->
        Log.i("tset", "TESTTTT");
        Text("TESTTT");
        if (isLoading) {
            Text("Loading...", modifier = Modifier.padding(innerPadding))
        } else {
            Column {
                FlightList(
                    modifier = Modifier.padding(innerPadding),
                    flights = viewModel.flights
                )
            }
        }

    }
}

@Composable
fun FlightList(
    modifier: Modifier = Modifier,
    flights: List<Flight>
) {
    LazyColumn(modifier = modifier) {
        items(flights) { flight ->
            FlightItem(flight = flight)
        }
    }
}

@Composable
fun FlightItem(flight: Flight) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Airline: ${flight.airline}")
            Text(text = "Departure Time: ${flight.departureAt.subSequence(0, 10)}")
            Text(text = "Return Time: ${flight.returnAt.subSequence(0, 10)}")
        }
        Text(text = "Price: ${flight.price} ")
    }
}
