package com.example.mad_project.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ViewModel.DetailScreenViewModel
import com.example.mad_project.Feature
import com.example.mad_project.HorizontalScrollableImageView
import com.example.mad_project.PlaceDetails
import com.example.mad_project.openGoogleMaps
import com.example.movieappmad24.components.Bars.SimpleBottomAppBar
import com.example.movieappmad24.components.Bars.SimpleTopAppBar

@Composable
fun DetailScreen(
    featurejson: String,
    viewModel: DetailScreenViewModel,
    navController: NavController
) {
    val isPassFeatureCalled = remember { mutableStateOf(false) }

    if (!isPassFeatureCalled.value) {
        viewModel.passFeature(featurejson)
        isPassFeatureCalled.value = true
    }

    val isLoading by remember { viewModel.isLoading }
    val feature by remember { viewModel.feature }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SimpleTopAppBar(
                title = if ((feature?.properties?.name?.length ?: 100) < 30) feature?.properties?.name ?: "Detail" else "Detail",
                navController = navController,
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Text("Loading...", modifier = Modifier.padding(innerPadding))
        } else {
            LaunchedEffect(featurejson) {
                viewModel.loadImages()
                viewModel.loadWikiInfo()
            }

            val commonPadding = Modifier.padding(0.dp)
            val elementSpacing = 8.dp

            Column(
                modifier = commonPadding
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
            ) {
                HorizontalScrollableImageView(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(elementSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            openGoogleMaps(
                                latitude = feature?.geometry?.coordinates?.get(1) ?: 0.0,
                                longitude = feature?.geometry?.coordinates?.get(0) ?: 0.0,
                                context = context
                            )
                        }
                    ) {
                        Text("Open in Google Maps")
                    }
                }
                Spacer(modifier = Modifier.height(elementSpacing))
                PlaceDetails(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}