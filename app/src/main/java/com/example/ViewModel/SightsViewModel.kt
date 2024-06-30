// SightsViewModel.kt
package com.example.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mad_project.Feature
import com.example.mad_project.FeatureCollection
import com.example.mad_project.common.fetchJson
import com.google.gson.Gson

class SightsViewModel : ViewModel() {
    private val _features = mutableStateListOf<Feature>()
    val features: List<Feature>
        get() = _features

    val isLoading = mutableStateOf(true)
    var isSortedAscending = mutableStateOf(true)
        private set

    // MutableState for selected sights
    val selectedSights = mutableStateListOf<Feature>()

    fun fetchSights(lat: Double, lng: Double) {
        isLoading.value = true
        val apiKey = "5ae2e3f221c38a28845f05b68f0604b0270b6d5840c560e9934b6302"
        val apiUrl = "https://api.opentripmap.com/0.1/en/places/radius?radius=5000&lon=$lng&lat=$lat&src_geom=wikidata&apikey=$apiKey"
        fetchJson(apiUrl) { jsonData, errorMessage ->
            if (errorMessage != null) {
                // Handle error
                Log.e("API FETCH ERROR", errorMessage)
            } else {
                val gson = Gson()
                val featureCollection = gson.fromJson(jsonData, FeatureCollection::class.java)

                _features.addAll(featureCollection.features)
                isLoading.value = false
            }
        }
    }

    fun sortByRating() {
        if (isSortedAscending.value) {
            _features.sortBy { it.properties?.rate }
        } else {
            _features.sortByDescending { it.properties?.rate }
        }
        isSortedAscending.value = !isSortedAscending.value
    }

    // Function to toggle selected sights
    fun toggleSightSelection(sight: Feature) {
        if (selectedSights.contains(sight)) {
            selectedSights.remove(sight)
        } else {
            selectedSights.add(sight)
        }
    }
}
