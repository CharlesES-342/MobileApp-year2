package com.example.workoutApp

data class Exercise(
    val name: String,
    val weight: Double,
    val reps: Int,
    val notes: String,
    val videoUri: String? = null // Optional field for video URL, points to gallery video
)
