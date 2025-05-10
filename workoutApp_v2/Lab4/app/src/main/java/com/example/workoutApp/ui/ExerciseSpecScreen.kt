package com.example.workoutApp.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workoutApp.Exercise

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExerciseSpecScreen(//passed from the previous screen
    viewModel: WorkoutViewModel,
    workoutIndex: Int,
    exerciseIndex: Int?, //can be null in the event of forming a new exercise
    navController: NavHostController
) {

    val workoutData by viewModel.workoutData.collectAsState()
    ///get the current workout
    val session = workoutData[workoutIndex]
    //get teh current exercise (get if null it is a new one)
    val existingExercise = exerciseIndex?.let { session.exercises[it] }


    var name by remember { mutableStateOf(existingExercise?.name ?: "") }
    var weight by remember { mutableStateOf(existingExercise?.weight.toString() ?: "") }
    var reps by remember { mutableStateOf(existingExercise?.reps?.toString() ?: "") }
    var notes by remember { mutableStateOf(existingExercise?.notes ?: "") }
    var URI by remember { mutableStateOf(existingExercise?.videoUri ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (existingExercise != null) "Edit Exercise" else "New Exercise",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Exercise Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        //weigth input
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (Kg)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Reps Input
        OutlinedTextField(
            value = reps,
            onValueChange = { reps = it },
            label = { Text("Repetitions") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        //notes input
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Additional Notes") },
            modifier = Modifier.fillMaxWidth().height(120.dp), // instead of fillMaxHeight
            maxLines = 5
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = {
                val newExercise = Exercise(name, weight.toDoubleOrNull() ?: 0.0, reps.toIntOrNull() ?: 0, notes, URI)
                viewModel.saveExercise(workoutIndex, exerciseIndex, newExercise)
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}


