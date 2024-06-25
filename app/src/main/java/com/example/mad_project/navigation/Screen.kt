package com.example.mad_project.navigation

const val JSON_ARGUMENT_KEY = "jsonString"
const val LOCATION_ARGUMENT_KEY = "locationJsonString"
const val DETAIL_ARGUMENT_KEY = "detail"

sealed class Screen(val route: String) {

    object HomeScreen : Screen(route = "home")

    object DestinationSelectScreen : Screen(route = "destination-select")

    object FlightsScreen : Screen(route = "flights/{$JSON_ARGUMENT_KEY}") {
        fun withJsonString(jsonString: String): String {
            return this.route.replace("{$JSON_ARGUMENT_KEY}", newValue = jsonString)
        }
    }

    object SightsScreen : Screen(route = "sights/{$LOCATION_ARGUMENT_KEY}") {
        fun withJsonString(locationJsonString: String): String {
            return this.route.replace("{$LOCATION_ARGUMENT_KEY}", newValue = locationJsonString)
        }
    }

    object DetailScreen : Screen(route = "detail/{$DETAIL_ARGUMENT_KEY}") {
        fun withInfo(info: String): String {
            return this.route.replace("{$DETAIL_ARGUMENT_KEY}", newValue = info)
        }
    }
}
