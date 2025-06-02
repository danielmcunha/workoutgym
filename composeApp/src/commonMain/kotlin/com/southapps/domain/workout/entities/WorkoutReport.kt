package com.southapps.domain.workout.entities

data class WorkoutReport(
    val workoutName: String,
    val workouts: List<WorkoutProgress>,
    val completionAverage: Int,
    val sequenceCount: Int,
    val lastWorkoutDuration: String,
    val lastWorkoutDate: String
)