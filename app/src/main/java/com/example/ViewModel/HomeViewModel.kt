package com.example.ViewModel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf

class HomeScreenViewModel : ViewModel() {
    val title = mutableStateOf("Trip Planner")
}