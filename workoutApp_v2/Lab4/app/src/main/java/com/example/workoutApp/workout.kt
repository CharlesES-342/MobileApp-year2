package com.example.workoutApp

import java.time.LocalDate

data class Workout(
    val title: String,
    val date: LocalDate,
    val exercises: List<Exercise>
)