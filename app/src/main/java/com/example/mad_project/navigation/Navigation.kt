package com.example.mad_project.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ViewModel.*
import com.example.mad_project.screens.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val flightsViewModel: FlightsViewModel = viewModel()
    val sightsViewModel: SightsViewModel = viewModel()
    val detailScreenViewModel: DetailScreenViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {

        composable(route = Screen.HomeScreen.route) {
            HomeScreen(
                viewModel = homeScreenViewModel,
                navController = navController
            )
        }

        composable(route = Screen.DestinationSelectScreen.route) {
            DestinationSelectScreen(
                navController = navController
            )
        }

        composable(
            route = Screen.FlightsScreen.route,
            arguments = listOf(navArgument(name = DESTINATION_ARGUMENT_KEY) { type = NavType.StringType })
        ) { backStackEntry ->
            val jsonString = backStackEntry.arguments?.getString(DESTINATION_ARGUMENT_KEY) ?: ""
            FlightsScreen(
                viewModel = flightsViewModel,
                navController = navController,
                jsonString = jsonString
            )
        }

        composable(
            route = Screen.SightsScreen.route,
            arguments = listOf(navArgument(name = LOCATION_ARGUMENT_KEY) { type = NavType.StringType })
        ) { backStackEntry ->
            val locationJsonString = backStackEntry.arguments?.getString(LOCATION_ARGUMENT_KEY) ?: ""
            SightsScreen(
                viewModel = sightsViewModel,
                navController = navController,
                locationJsonString = locationJsonString
            )
        }

        composable(
            route = Screen.DetailScreen.route,
            arguments = listOf(navArgument(name = DETAIL_ARGUMENT_KEY) { type = NavType.StringType })
        ) { backStackEntry ->
            DetailScreen(
                viewModel = detailScreenViewModel,
                featurejson = backStackEntry.arguments?.getString(DETAIL_ARGUMENT_KEY) ?: "",
                navController = navController
            )
        }
    }
}
