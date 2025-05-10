package com.example.workoutApp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutApp.Workout
import com.example.workoutApp.ui.theme.BackdropRed


@Composable
fun WorkoutSearchScreen(
    viewModel: WorkoutViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") } // a variable to keep the value of the TextField
    val workoutData by viewModel.workoutData.collectAsState() // use the viewModel to observe the UI state - pass teh data from the viewmodel

    Column(
        modifier = modifier
            .padding(16.dp) // add padding around the column
            .fillMaxWidth() // Ensures the Column takes up the full width
    ) {
        WorkoutList(workoutData, navController, modifier) // whenever the UI state changes, WorkoutList will get refreshed automatically
    }
}

@Composable
fun WorkoutList(data: List<Workout>, navController: NavHostController, modifier: Modifier = Modifier){
    val context = LocalContext.current // get the activity context within a composable function
    Text(text = "Workout count: ${data.size}")
    LazyColumn(
        modifier = Modifier.fillMaxWidth() // Ensures that the LazyColumn takes up the full width of the screen
    ) {
        items(data.size) { index ->
            Box(
                modifier = modifier
                    .fillMaxWidth() // Ensures the Box takes up the full width
                    .height(100.dp) // Set a fixed height for each item (adjust to your needs)
                    .clickable {
                        // handle the onClick event to the list item
                        Toast
                            .makeText(context, data[index].title + " selected", Toast.LENGTH_SHORT)
                            .show() // display a Toast when an item in the list is clicked
                        // navigate to the WorkoutDetailScreen
                        navController.navigate("WorkoutDetailScreen/$index")
                    }
                    .background(
                        color = BackdropRed,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = data[index].title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp)) //space between title and date
                    Text(
                        text = data[index].date.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            // Add a Spacer to create a gap between the items
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
