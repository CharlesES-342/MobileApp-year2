package com.example.workoutApp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "weatherPreference")

class WeatherPreferenceManager(private val context: Context) {
    companion object {
        private val CITYNAME_KEY = stringPreferencesKey("cityname")
    }
    val citynameFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            // Retrieve the cityname value, returning null if not set
            preferences[CITYNAME_KEY]
        }
    suspend fun saveCityName(cityname: String) {
        context.dataStore.edit { preferences ->
            // Save the cityname in DataStore
            preferences[CITYNAME_KEY] = cityname
        }
    }

}