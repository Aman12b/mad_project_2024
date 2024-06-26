package com.example.ViewModel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class TripsViewModel(private val context: Context) : ViewModel() {

    val savedTrips = mutableStateListOf<String>()

    init {
        loadSavedTrips()
    }

    private fun loadSavedTrips() {
        viewModelScope.launch {
            val trips = withContext(Dispatchers.IO) {
                val filesDir = context.filesDir
                filesDir.listFiles()?.filter { it.extension == "json" }?.map { it.readText() } ?: emptyList()
            }
            savedTrips.addAll(trips)
        }
    }

    fun saveTrip(jsonString: String) {
        val fileName = "trip_${System.currentTimeMillis()}.json"
        val file = File(context.filesDir, fileName)
        file.writeText(jsonString)
        savedTrips.add(jsonString)
    }
}
