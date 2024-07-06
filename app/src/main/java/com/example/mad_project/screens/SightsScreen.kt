package com.example.mad_project.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ViewModel.SightsViewModel
import com.example.mad_project.FeatureList
import com.example.mad_project.navigation.Screen
import com.example.movieappmad24.components.Bars.SimpleTopAppBar
import com.example.movieappmad24.components.Bars.TopAppBarAction
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.core.content.FileProvider


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
                    onClick = { viewModel.sortByRating() },
                    contentDescription = "Sort"
                )
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Text("Loading...", modifier = Modifier.padding(innerPadding))
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                FeatureList(
                    modifier = Modifier.weight(1f).padding(innerPadding),
                    navController = navController,
                    viewModel = viewModel,
                    onFeatureClick = { feature -> viewModel.toggleSightSelection(feature) }
                )

                // navigate to SummaryScreen
                Button(
                    onClick = {
                        val updatedJsonObject = JSONObject(locationJsonString).apply {
                            put("selectedSights", JSONArray().apply {
                                viewModel.selectedSights.forEach { sight ->
                                    put(JSONObject().apply {
                                        put("name", sight.properties?.name)
                                    })
                                }
                            })
                        }
                        val summaryJson = updatedJsonObject.toString()
                        navController.navigate(route = Screen.SummaryScreen.withJsonString(summaryJson))
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                ) {
                    Text("Confirm Sights")
                }
            }
        }
    }
}
