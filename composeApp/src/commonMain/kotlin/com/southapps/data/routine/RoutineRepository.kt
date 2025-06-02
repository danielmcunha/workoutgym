package com.southapps.data.routine

import com.southapps.domain.routine.entities.WorkoutRoutine

interface RoutineRepository {
    suspend fun createRoutine(chickenPath: String?, routine: WorkoutRoutine)
    suspend fun getRoutine(userPath: String): List<WorkoutRoutine>
    suspend fun removeRoutine(chickenPath: String?, routineId: String)
}