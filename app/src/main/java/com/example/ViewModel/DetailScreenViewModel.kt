package com.example.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mad_project.Feature
import com.example.mad_project.common.getallImages
import com.example.mad_project.fromJson
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.State
import com.example.mad_project.common.getWikiInfo

class DetailScreenViewModel : ViewModel() {
    // Define your state here
    private val _feature = mutableStateOf<Feature?>(null)
    val feature: State<Feature?> = _feature

    val isLoading = mutableStateOf(true)
    val isLoadingImage = mutableStateOf(true)
    val isLoadingWiki = mutableStateOf(true)

    fun passFeature(featurejson: String){
        val gson = Gson()
        val temp_feature: Feature = gson.fromJson(featurejson)
        _feature.value = temp_feature
        Log.i("passFeature",featurejson)
    }

    init{
        if(_feature.value?.properties != null && _feature.value?.properties?.name != null) {

            if(_feature.value!!.properties?.images == null){
                _feature.value!!.properties?.images = mutableListOf()
            }

        }
        Log.i("init", "Feature updated: ${_feature.value?.properties?.images.toString()}")
        isLoading.value = false
    }

    fun loadImages(){
        runBlocking {
            getallImages(feature.value?.properties?.name.toString()) { imageLinks ->
                _feature.value = feature.value?.copy(
                    properties = feature.value?.properties?.copy(
                        images = imageLinks.toMutableList()
                    )
                )
                Log.i("loadimages", "Feature updated: ${_feature.value?.properties?.images.toString()}")
            }
        }
        isLoadingImage.value = false
    }

    fun loadWikiInfo(){
        runBlocking {
            getWikiInfo(feature.value?.properties?.name.toString()) { wikiInfo ->
                feature.value?.properties?.description = wikiInfo

                Log.i("loadWikiInfo", "Feature updated: ${feature.value?.properties?.description.toString()}")
            }
        }
        isLoadingWiki.value = false
    }
}