package com.example.mad_project.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ViewModel.FlightsViewModel
import com.example.mad_project.FeatureList
import com.example.mad_project.FlightList
import com.example.movieappmad24.components.Bars.SimpleTopAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FlightsScreen(
    navController: NavController,
    viewModel: FlightsViewModel
) {
    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Flights", navController = navController)
        }
    ) { innerPadding ->
        Column {
            FlightList(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}