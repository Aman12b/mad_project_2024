package com.example.mad_project.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ViewModel.DetailScreenViewModel
import com.example.ViewModel.SightsViewModel
import com.example.mad_project.screens.DetailScreen
import com.example.mad_project.screens.SightsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController() // create a NavController instance

    val sightsViewModel: SightsViewModel = viewModel()
    val detailScreenViewModel: DetailScreenViewModel = viewModel()

    NavHost(navController = navController, // pass the NavController to NavHost
        startDestination = Screen.SightsScreen.route) {  // pass a start destination
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