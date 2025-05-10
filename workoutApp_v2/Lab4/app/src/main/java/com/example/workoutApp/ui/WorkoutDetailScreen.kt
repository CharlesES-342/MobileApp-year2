package com.example.workoutApp.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutApp.ui.theme.BackdropRed
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutDetailScreen(
    viewModel: WorkoutViewModel,
    workoutIndex: Int?,
    navController: NavHostController
) {
    val workoutData by viewModel.workoutData.collectAsState()

    if (workoutIndex == null || workoutIndex !in workoutData.indices) return
    val session = workoutData[workoutIndex]

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn {
                items(session.exercises.size) { index ->
                    val exercise = session.exercises[index]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("ExerciseSpecScreen/$workoutIndex/$index")
                            }
                            .background(
                                color = BackdropRed,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = exercise.name,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f) // takes up remaining space
                            )
                            Text(
                                text = exercise.weight.toString(),
                                style = MaterialTheme.typography.headlineMedium, // large font
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Text(
                                text ="Kg",
                                style = MaterialTheme.typography.headlineSmall, // large font
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = exercise.reps.toString() + " reps",
                                style = MaterialTheme.typography.bodyLarge, // large font
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }

                }
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate("AddExerciseScreen")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Exercise"
            )
        }
    }
}
