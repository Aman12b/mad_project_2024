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
import com.example.mad_project.FeatureList
import com.example.ViewModel.SightsViewModel
import com.example.movieappmad24.components.Bars.SimpleBottomAppBar
import com.example.movieappmad24.components.Bars.SimpleTopAppBar

@Composable
fun SightsScreen(
    viewModel: SightsViewModel,
    navController: NavController
) {
    val isLoading by remember { viewModel.isLoading }

    Scaffold (
        topBar = {
            SimpleTopAppBar("Sights") {
                //navController.popBackStack()
            }
        },
        bottomBar = {
            SimpleBottomAppBar(
                navController = navController
            )
        }
    ){ innerPadding ->
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
