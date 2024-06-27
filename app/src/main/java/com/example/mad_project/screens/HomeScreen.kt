package com.example.mad_project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ViewModel.HomeScreenViewModel
import com.example.mad_project.navigation.Screen

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    navController: NavController
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val contentColor = MaterialTheme.colorScheme.onBackground

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Trip Planner",
            style = MaterialTheme.typography.headlineLarge,
            color = contentColor
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            navController.navigate(route = Screen.DestinationSelectScreen.route)
        }) {
            Text(text = "Start Planning")
        }
    }
}
