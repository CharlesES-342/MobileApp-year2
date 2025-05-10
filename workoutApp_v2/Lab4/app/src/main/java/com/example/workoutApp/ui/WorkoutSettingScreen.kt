package com.example.workoutApp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WorkoutSettingScreen(
    settingViewModel: SettingViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val cityname by settingViewModel.cityname.observeAsState("")
    var text by rememberSaveable { mutableStateOf (cityname) }//use a local variable to keep track of the value of the TextField
    Column(
        modifier =
        modifier.padding(16.dp)//add padding all around
    ) {
        Text(
            text = "Set default location:",
            modifier = modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)//add padding between child elements
        )
        Row {
            TextField(
                value = text,
                label = { Text("Default location") },
                maxLines = 1,
                onValueChange = {
                    text = it//update the value of the TextField
                    settingViewModel.saveCityName(text) },//save the new city name into the preference datastore
                modifier = modifier
                    .padding(0.dp, 0.dp, 0.dp, 16.dp)//add padding between child element
                    .weight(1f)
            )
        }
    }
}