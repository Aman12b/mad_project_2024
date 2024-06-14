package com.example.mad_project.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.ViewModel.FlightsViewModel
import com.example.movieappmad24.components.Bars.SimpleTopAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FlightsScreen(
    navController: NavController,
    viewModel: FlightsViewModel
) {
    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Select Destination", navController = navController)
        }
    ) {  innerPadding ->
        // Content
    }
}