package com.example.mad_project.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ViewModel.TripsViewModel
import com.example.ViewModel.TripsViewModelFactory
import com.example.movieappmad24.components.Bars.SimpleBottomAppBar
import com.example.movieappmad24.components.Bars.SimpleTopAppBar

@Composable
fun TripsScreen(navController: NavController, jsonString: String) {
    val context = LocalContext.current
    val viewModel: TripsViewModel = viewModel(factory = TripsViewModelFactory(context))
    var saveStatus by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Trips", navController = navController)
        },
        bottomBar = {
            SimpleBottomAppBar(navController = navController, jsonString = jsonString)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Current Search Parameters", modifier = Modifier.padding(bottom = 16.dp))
            Text(jsonString, modifier = Modifier.padding(bottom = 16.dp))

            Button(
                onClick = {
                    viewModel.saveTrip(jsonString)
                    saveStatus = "Trip saved successfully!"
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Save")
            }

            if (saveStatus.isNotEmpty()) {
                Text(saveStatus)
            }

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Back")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Saved Trips", modifier = Modifier.padding(bottom = 16.dp))
            LazyColumn {
                items(viewModel.savedTrips) { trip ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                // Handle card click if needed
                            }
                    ) {
                        Text(
                            text = trip,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
