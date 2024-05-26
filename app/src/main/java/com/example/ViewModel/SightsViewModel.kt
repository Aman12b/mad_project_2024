package com.example.ViewModel
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

    // Add a loading state
    val isLoading = mutableStateOf(true)

    init {
        fetchJson("https://api.opentripmap.com/0.1/en/places/radius?radius=2000&lon=16.37208&lat=48.20849&src_geom=wikidata&apikey=5ae2e3f221c38a28845f05b68f0604b0270b6d5840c560e9934b6302") { jsonData ->
            val gson = Gson()
            val featureCollection = gson.fromJson(jsonData, FeatureCollection::class.java)
            /*
            featureCollection.features.forEach { feature ->
               feature.properties?.images = getallImages(feature.properties?.name.toString())
            }
            */
            _features.addAll(featureCollection.features)
            // Set loading to false after data is loaded
            isLoading.value = false
        }
    }
}
