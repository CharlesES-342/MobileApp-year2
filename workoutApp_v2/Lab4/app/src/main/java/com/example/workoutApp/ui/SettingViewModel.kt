package com.example.workoutApp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.workoutApp.data.WeatherPreferenceManager
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingViewModel(application: Application) : AndroidViewModel(application) {
    private val weatherPreferenceManager = WeatherPreferenceManager(application)
    // Expose cityname as a state that composables can observe
    val cityname: LiveData<String> = weatherPreferenceManager.citynameFlow
        .map { it ?: "london" } // Provide a default value if null
        .asLiveData()
    // Method to save cityname
    fun saveCityName(newCityName: String) {
        viewModelScope.launch {
            weatherPreferenceManager.saveCityName(newCityName)
        }
    }

}