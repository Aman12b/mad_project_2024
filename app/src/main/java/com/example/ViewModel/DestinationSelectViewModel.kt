package com.example.ViewModel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad_project.classes.City
import com.example.mad_project.classes.Coordinates
import com.example.mad_project.classes.CountryData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class DestinationSelectViewModel(
    private val context: Context,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val destinations = mutableStateListOf<String>()
    val filteredOriginDestinations = mutableStateListOf<String>()
    val filteredDestinationDestinations = mutableStateListOf<String>()
    val continents = mutableStateListOf<String>()
    val countries = mutableMapOf<String, MutableList<String>>()
    val cities = mutableMapOf<String, MutableList<City>>()
    val originSearchQuery = mutableStateOf(savedStateHandle["originSearchQuery"] ?: "")
    val destinationSearchQuery = mutableStateOf(savedStateHandle["destinationSearchQuery"] ?: "")
    val selectedDestination = mutableStateOf(savedStateHandle["selectedDestination"] ?: "")
    val startLocation = mutableStateOf(savedStateHandle["startLocation"] ?: "")
    val date1 = mutableStateOf(savedStateHandle["date1"] ?: "")
    val date2 = mutableStateOf(savedStateHandle["date2"] ?: "")
    val directFlight = mutableStateOf(savedStateHandle["directFlight"] ?: false)
    val luggageCount = mutableStateOf(savedStateHandle["luggageCount"] ?: 0)
    val isLoading = mutableStateOf(false)

    val selectedOriginContinent = mutableStateOf<String?>(savedStateHandle["selectedOriginContinent"])
    val selectedOriginCountry = mutableStateOf<String?>(savedStateHandle["selectedOriginCountry"])

    val selectedDestinationContinent = mutableStateOf<String?>(savedStateHandle["selectedDestinationContinent"])
    val selectedDestinationCountry = mutableStateOf<String?>(savedStateHandle["selectedDestinationCountry"])

    init {
        loadDestinations()
    }

    private fun loadDestinations() {
        viewModelScope.launch {
            isLoading.value = true
            withContext(Dispatchers.IO) {
                val inputStream = context.assets.open("country_data.json")
                val reader = InputStreamReader(inputStream)
                val data = Gson().fromJson(reader, CountryData::class.java)
                reader.close()

                val allCitiesWithIata = data.continents.flatMap { continent ->
                    continents.add(continent.name)
                    continent.countries.flatMap { country ->
                        countries.getOrPut(continent.name) { mutableListOf() }.add(country.name)
                        cities.getOrPut(country.name) { mutableListOf() }.addAll(country.cities)
                        country.cities.flatMap { city ->
                            city.iata_codes.map { iata -> "${city.name} ($iata)" }
                        }
                    }
                }

                destinations.addAll(allCitiesWithIata)
                filteredOriginDestinations.addAll(allCitiesWithIata)
                filteredDestinationDestinations.addAll(allCitiesWithIata)
                isLoading.value = false
            }
        }
    }

    fun onOriginSearchQueryChange(query: String) {
        originSearchQuery.value = query
        savedStateHandle["originSearchQuery"] = query
        filterDestinations(true)
    }

    fun onDestinationSearchQueryChange(query: String) {
        destinationSearchQuery.value = query
        savedStateHandle["destinationSearchQuery"] = query
        filterDestinations(false)
    }

    private fun filterDestinations(isOrigin: Boolean) {
        val query = if (isOrigin) originSearchQuery.value else destinationSearchQuery.value
        val filteredList = if (query.isEmpty()) {
            destinations
        } else {
            destinations.filter { it.contains(query, ignoreCase = true) }
        }
        if (isOrigin) {
            filteredOriginDestinations.clear()
            filteredOriginDestinations.addAll(filteredList)
        } else {
            filteredDestinationDestinations.clear()
            filteredDestinationDestinations.addAll(filteredList)
        }
    }

    fun getFilteredLocations(nestedSearch: Boolean, isOrigin: Boolean): List<String> {
        val query = if (isOrigin) originSearchQuery.value else destinationSearchQuery.value

        return if (nestedSearch) {
            val selectedContinent = if (isOrigin) selectedOriginContinent.value else selectedDestinationContinent.value
            val selectedCountry = if (isOrigin) selectedOriginCountry.value else selectedDestinationCountry.value

            val locations = when {
                selectedContinent == null -> continents
                selectedCountry == null -> countries[selectedContinent] ?: emptyList()
                else -> cities[selectedCountry]?.flatMap { city ->
                    city.iata_codes.map { iata -> "${city.name} ($iata)" }
                } ?: emptyList()
            }

            if (query.isEmpty()) {
                locations
            } else {
                locations.filter { it.contains(query, ignoreCase = true) }
            }
        } else {
            if (isOrigin) filteredOriginDestinations else filteredDestinationDestinations
        }
    }

    fun onSelectDestination(destination: String, isOrigin: Boolean) {
        if (isOrigin) {
            selectedOriginContinent.value = null
            selectedOriginCountry.value = null
            startLocation.value = destination
            savedStateHandle["startLocation"] = destination
        } else {
            selectedDestinationContinent.value = null
            selectedDestinationCountry.value = null
            selectedDestination.value = destination
            savedStateHandle["selectedDestination"] = destination
        }
    }

    fun setStartDate(date: String) {
        date1.value = date
        savedStateHandle["date1"] = date
    }

    fun setEndDate(date: String) {
        date2.value = date
        savedStateHandle["date2"] = date
    }

    fun toggleDirectFlight() {
        directFlight.value = !directFlight.value
        savedStateHandle["directFlight"] = directFlight.value
    }

    fun increaseLuggageCount() {
        luggageCount.value += 1
        savedStateHandle["luggageCount"] = luggageCount.value
    }

    fun decreaseLuggageCount() {
        if (luggageCount.value > 0) {
            luggageCount.value -= 1
            savedStateHandle["luggageCount"] = luggageCount.value
        }
    }

    fun getCoordinates(cityWithIata: String): Coordinates? {
        val cityName = cityWithIata.substringBefore(" (")
        for (countryCities in cities.values) {
            for (city in countryCities) {
                if (city.name == cityName) {
                    return city.coordinates
                }
            }
        }
        return null
    }
}
