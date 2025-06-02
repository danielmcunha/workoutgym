package com.southapps.data.workout

import com.southapps.domain.workout.entities.Workout
import com.southapps.domain.workout.entities.WorkoutHistory
import com.southapps.domain.workout.entities.WorkoutProgress

interface WorkoutRepository {

    suspend fun getWorkouts(chickenId: String?) : List<Workout>
    suspend fun getWorkoutFromPath(path: String): Workout
    suspend fun createOrEdit(workout: Workout, chickenId: String?)

    suspend fun saveHistory(workoutHistory: WorkoutHistory)

    suspend fun getHistory(name: String) : WorkoutHistory?

    suspend fun saveCurrentWorkout(workoutProgress: WorkoutProgress)

    suspend fun getCurrentWorkout() : WorkoutProgress?

    suspend fun deleteCurrentWorkout(id: String)
}