package com.example.movieappmad24.components.Bars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TripOrigin
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mad_project.navigation.Screen

@Composable
fun SimpleBottomAppBar(navController: NavController, jsonString: String) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screens = listOf(
        BottomBarScreen.Search,
        BottomBarScreen.Trips
    )

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    if (screen.route == BottomBarScreen.Search.route) {
                        navController.popBackStack()
                    } else {
                        navController.navigate("${screen.route}/$jsonString")
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                }
            )
        }
    }
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Search : BottomBarScreen(
        route = "search",
        title = "Search",
        icon = Icons.Filled.Search
    )

    object Trips : BottomBarScreen(
        route = "trips",
        title = "Trips",
        icon = Icons.Filled.TripOrigin
    )
}
