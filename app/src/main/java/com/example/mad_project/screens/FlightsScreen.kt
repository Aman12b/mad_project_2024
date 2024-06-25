package com.example.mad_project.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ViewModel.FlightsViewModel
import com.example.mad_project.FlightList
import com.example.movieappmad24.components.Bars.SimpleTopAppBar
import org.json.JSONObject

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
        Column {
            if (viewModel.isLoading.value) {
                // Display a loading indicator here
                Text("Loading...")
            } else {
                // Display the list of flights
                FlightList(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
