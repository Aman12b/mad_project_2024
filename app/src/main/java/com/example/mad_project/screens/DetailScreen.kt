package com.example.mad_project.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ViewModel.DetailScreenViewModel
import com.example.mad_project.classes.Feature
import com.example.mad_project.HorizontalScrollableImageView
import com.example.mad_project.PlaceDetails
import com.example.mad_project.fromJson
import com.example.mad_project.openGoogleMaps
import com.example.movieappmad24.components.Bars.SimpleBottomAppBar
import com.example.movieappmad24.components.Bars.SimpleTopAppBar
import com.google.gson.Gson

@Composable
fun DetailScreen(featurejson: String,
                 viewModel: DetailScreenViewModel,
                 navController: NavController
) {
    viewModel.passFeature(featurejson)
    viewModel.loadimages()
    val isLoading by remember { viewModel.isLoading }
    val gson = Gson()
    val feature: Feature = gson.fromJson(featurejson)
    val context = LocalContext.current
    Scaffold (
        topBar = {
            SimpleTopAppBar(title = if((feature.properties?.name?.length?:100)<30) feature.properties?.name?:"Detail" else "Detail", navController = navController)
        },
        bottomBar = {
            SimpleBottomAppBar(
                navController = navController
            )
        }
    ){ innerPadding ->
        if (isLoading) {
            Text("Loading...")
        } else {
            val commonPadding = Modifier.padding(0.dp)
            val elementSpacing = 8.dp

            Column(modifier = commonPadding) {
                HorizontalScrollableImageView(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                Spacer(modifier = Modifier.height(elementSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            openGoogleMaps(
                                latitude = feature.geometry?.coordinates?.get(1) ?: 0.0,
                                longitude = feature.geometry?.coordinates?.get(0) ?: 0.0,
                                context = context
                            )
                        }
                    ) {
                        Text("Open in Google Maps")
                    }
                }
                Spacer(modifier = Modifier.height(elementSpacing))
                PlaceDetails(
                    feature = feature,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
