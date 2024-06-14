package com.example.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DestinationSelectViewModel : ViewModel() {
    val destinations = mutableStateListOf<String>()
    val isLoading = mutableStateOf(false)

    init {
        loadDestinations()
    }

    private fun loadDestinations() {
        isLoading.value = true
        destinations.addAll(listOf("Paris", "New York", "Tokyo", "Sydney", "Rome"))
        isLoading.value = false
    }
}