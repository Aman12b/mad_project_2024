package com.example.mad_project.navigation

const val DESTINATION_ARGUMENT_KEY = "destination"
const val LOCATION_ARGUMENT_KEY = "location"
const val DETAIL_ARGUMENT_KEY = "detail"
const val TRIP_ARGUMENT_KEY = "trip"

sealed class Screen(val route: String) {

    object HomeScreen : Screen(route = "home")

    object DestinationSelectScreen : Screen(route = "destination-select")

    object FlightsScreen : Screen(route = "flights/{$DESTINATION_ARGUMENT_KEY}") {
        fun withJsonString(destination: String): String {
            return this.route.replace("{$DESTINATION_ARGUMENT_KEY}", newValue = destination)
        }
    }

    object SightsScreen : Screen(route = "sights/{$LOCATION_ARGUMENT_KEY}") {
        fun withJsonString(location: String): String {
            return this.route.replace("{$LOCATION_ARGUMENT_KEY}", newValue = location)
        }
    }

    object DetailScreen : Screen(route = "detail/{$DETAIL_ARGUMENT_KEY}") {
        fun withInfo(detail: String): String {
            return this.route.replace("{$DETAIL_ARGUMENT_KEY}", newValue = detail)
        }
    }

    object TripsScreen : Screen(route = "trips/{$TRIP_ARGUMENT_KEY}") {
        fun withJsonString(trip: String): String {
            return this.route.replace("{$TRIP_ARGUMENT_KEY}", newValue = trip)
        }
    }
}
