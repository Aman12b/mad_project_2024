package com.example.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mad_project.Feature
import com.example.mad_project.common.getallImages
import com.example.mad_project.fromJson
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

class DetailScreenViewModel : ViewModel() {
    // Define your state here
    private val _feature = mutableStateOf<Feature?>(null)
    val feature: Feature?
        get() = _feature.value
    val isLoading = mutableStateOf(true)

    fun passFeature(featurejson: String){
        val gson = Gson()
        val temp_feature: Feature = gson.fromJson(featurejson)
        _feature.value = temp_feature
    }

    fun loadimages(){
        if(_feature.value?.properties != null && _feature.value?.properties?.name != null) {

            if(_feature.value!!.properties?.images == null){
                _feature.value!!.properties?.images = mutableListOf()
            }
            _feature.value!!.properties?.images?.add("https://image.geo.de/30139388/t/Dl/v3/w1440/r0/-/wien-f-175321055-jpg--79261-.jpg")
            _feature.value!!.properties?.images?.add("https://image.geo.de/30139388/t/Dl/v3/w1440/r0/-/wien-f-175321055-jpg--79261-.jpg")
            _feature.value!!.properties?.images?.add("https://image.geo.de/30139388/t/Dl/v3/w1440/r0/-/wien-f-175321055-jpg--79261-.jpg")

            /*
            _feature.value!!.properties?.name?.let {
                runBlocking {
                    getallImages(it) { imageUrls ->
                        // Use the image URLs
                        for (url in imageUrls) {
                            _feature.value!!.properties?.images?.add(url)
                        }
                    }
                }
            }
            */
        }
        isLoading.value = false
    }
}