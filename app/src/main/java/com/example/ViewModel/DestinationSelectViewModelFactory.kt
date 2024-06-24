package com.example.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DestinationSelectViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DestinationSelectViewModel::class.java) -> {
                DestinationSelectViewModel(context) as T
            }
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}