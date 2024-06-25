package com.example.mad_project.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ViewModel.SightsViewModel
import com.example.mad_project.FeatureList
import com.example.movieappmad24.components.Bars.SimpleBottomAppBar
import com.example.movieappmad24.components.Bars.SimpleTopAppBar

@Composable
fun SightsScreen(
    viewModel: SightsViewModel,
    navController: NavController,
    jsonString: String
) {
    val isLoading by remember { viewModel.isLoading }
    val isSortedAscending by remember { viewModel.isSortedAscending }

    Scaffold(
        topBar = {
            SimpleTopAppBar(
                title = "Sights",
                onSortClick = { viewModel.sortByRating() },
                isSortedAscending = isSortedAscending,
                navController = navController
            )
        },
        bottomBar = {
            SimpleBottomAppBar(
                navController = navController
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
