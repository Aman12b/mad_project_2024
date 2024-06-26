package com.example.ViewModel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

class DestinationSelectViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()
        return when {
            modelClass.isAssignableFrom(DestinationSelectViewModel::class.java) -> {
                DestinationSelectViewModel(context, savedStateHandle) as T
            }
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
