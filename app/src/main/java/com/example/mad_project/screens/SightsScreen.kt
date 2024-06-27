package com.example.mad_project.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ViewModel.SightsViewModel
import com.example.mad_project.FeatureList
import com.example.movieappmad24.components.Bars.SimpleTopAppBar
import com.example.movieappmad24.components.Bars.TopAppBarAction
import org.json.JSONObject

@Composable
fun SightsScreen(
    viewModel: SightsViewModel,
    navController: NavController,
    locationJsonString: String
) {
    val jsonObject = JSONObject(locationJsonString)
    val lat = jsonObject.optDouble("destinationLat")
    val lng = jsonObject.optDouble("destinationLng")

    LaunchedEffect(Unit) {
        if (!lat.isNaN() && !lng.isNaN()) {
            viewModel.fetchSights(lat, lng)
        }
    }

    val isLoading by remember { viewModel.isLoading }
    val isSortedAscending by remember { viewModel.isSortedAscending }



    Scaffold(
        topBar = {
            SimpleTopAppBar(
                title = "Sights",
                navController = navController,
                sortingAction = TopAppBarAction(
                    icon = if (isSortedAscending) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    onClick = { /* Handle sorting click */ },
                    contentDescription = "Sort"
                )
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Text("Loading...", modifier = Modifier.padding(innerPadding))
        } else {
            Column {
                FeatureList(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
