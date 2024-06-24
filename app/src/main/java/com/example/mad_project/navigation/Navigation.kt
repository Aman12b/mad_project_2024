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
import com.example.ViewModel.DestinationSelectViewModel
import com.example.ViewModel.DetailScreenViewModel
import com.example.ViewModel.FlightsViewModel
import com.example.ViewModel.HomeScreenViewModel
import com.example.ViewModel.SightsViewModel
import com.example.mad_project.screens.DestinationSelectScreen
import com.example.mad_project.screens.DetailScreen
import com.example.mad_project.screens.HomeScreen
import com.example.mad_project.screens.SightsScreen
import com.example.mad_project.screens.FlightsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val navController = rememberNavController()

    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val flightsViewModel: FlightsViewModel = viewModel()

    val sightsViewModel: SightsViewModel = viewModel()
    val detailScreenViewModel: DetailScreenViewModel = viewModel()

    // for testing purposes changed to FlightsScreen
    NavHost(navController = navController,
        startDestination = Screen.FlightsScreen.route) {

        composable(
            route = Screen.HomeScreen.route
        ){
            HomeScreen(
                viewModel = homeScreenViewModel,
                navController = navController
            )
        }

        composable(
            route = Screen.DestinationSelectScreen.route
        ) {
            DestinationSelectScreen(
                navController = navController,
                //viewModel = destinationSelectViewModel
            )
        }

        composable(
            route = Screen.FlightsScreen.route
        ) {
            FlightsScreen(
                viewModel = flightsViewModel,
                navController = navController
            )
        }

        composable(
            route = Screen.SightsScreen.route,
            //arguments = listOf(navArgument(name = DETAIL_ARGUMENT_KEY) {type = NavType.StringType})
        ) { backStackEntry ->
            SightsScreen(
                viewModel = sightsViewModel,
                //location = backStackEntry.arguments?.getString(DETAIL_ARGUMENT_KEY),
                navController = navController
            )
        }

        composable(route = Screen.DetailScreen.route,
            arguments = listOf(navArgument(name = DETAIL_ARGUMENT_KEY) {type = NavType.StringType})
        ){backStackEntry ->
            DetailScreen(
                viewModel = detailScreenViewModel,
                featurejson = backStackEntry.arguments?.getString(DETAIL_ARGUMENT_KEY)?:"",
                navController = navController)
        }
    }
}