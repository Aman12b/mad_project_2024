package com.example.mad_project.navigation

const val LOCATION_ARGUMENT_KEY = "location"
const val DETAIL_ARGUMENT_KEY = "detail"
sealed class Screen(val route: String) {

    object HomeScreen : Screen("home")

    object DestinationSelectScreen : Screen("destination-select")

    object FlightsScreen : Screen("flights")
    object SightsScreen : Screen("sights")
    /*object SightsScreen : Screen("sights/{$LOCATION_ARGUMENT_KEY}") {
        fun withInfo(info: String): String {
            return this.route.replace(oldValue = "{$LOCATION_ARGUMENT_KEY}", newValue = info.toString())
        }
    }*/
    object DetailScreen : Screen("detail/{$DETAIL_ARGUMENT_KEY}") {
        fun withInfo(info: String): String {
            return this.route.replace(oldValue = "{$DETAIL_ARGUMENT_KEY}", newValue = info.toString())
        }
    }
}