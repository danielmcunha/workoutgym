package com.southapps.data.session

import com.southapps.domain.chicken.entities.ChickenSummary
import com.southapps.domain.user.entities.User
import com.southapps.domain.user.entities.UserType
import com.southapps.domain.workout.entities.MuscleGroup
import com.southapps.domain.workout.entities.WorkoutExercise
import com.southapps.domain.workout.entities.WorkoutExerciseSeries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface UserSession {
    val currentUser: StateFlow<User?>
    val chickens: StateFlow<List<ChickenSummary>>
    val sessionError: StateFlow<UserSessionError?>
    fun isUserLogged(): Boolean
    fun isUserMaster(): Boolean
    fun getAllWorkoutExercises(): List<WorkoutExercise>
    fun setSessionError(error: UserSessionError?)
}

interface MutableUserSession : UserSession {
    fun updateUser(user: User?)
    fun updateChickens(list: List<ChickenSummary>)
    fun clearAllSessionData()
}

class UserSessionManager : MutableUserSession {

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> get() = _currentUser

    private val _chickens = MutableStateFlow<List<ChickenSummary>>(emptyList())
    override val chickens: StateFlow<List<ChickenSummary>> get() = _chickens

    private val _sessionError = MutableStateFlow<UserSessionError?>(null)
    override val sessionError: StateFlow<UserSessionError?> = _sessionError.asStateFlow()

    override fun isUserLogged() = currentUser.value != null

    override fun isUserMaster() = currentUser.value?.type == UserType.Master

    override fun updateUser(user: User?) {
        _currentUser.value = user
    }

    override fun setSessionError(error: UserSessionError?) {
        _sessionError.value = error
    }

    override fun updateChickens(list: List<ChickenSummary>) {
        _chickens.value = list
    }

    override fun clearAllSessionData() {
        _currentUser.value = null
        _chickens.value = emptyList()
        _sessionError.value = null
    }

    override fun getAllWorkoutExercises(): List<WorkoutExercise> {
        return listOf(
            WorkoutExercise(
                id = "1",
                name = "Supino Reto",
                category = "Peito",
                series = listOf(
                    WorkoutExerciseSeries(name = "15", value = "10 kg"),
                    WorkoutExerciseSeries(name = "12", value = "15kg")
                ),
                note = "drop-set na última série",
                muscleGroup = MuscleGroup.CHEST,
                order = 0
            ),
            WorkoutExercise(
                id = "2",
                name = "Agachamento Livre",
                category = "Pernas",
                series = listOf(
                    WorkoutExerciseSeries(name = "12", value = "20 kg"),
                    WorkoutExerciseSeries(name = "10", value = "30 kg")
                ),
                note = "drop set",
                muscleGroup = MuscleGroup.LEG,
                order = 1
            ),
            WorkoutExercise(
                id = "3",
                name = "Puxada Frontal",
                category = "Costas",
                series = listOf(
                    WorkoutExerciseSeries(name = "12")
                ),
                note = "fazer na polia",
                order = 2,
                muscleGroup = MuscleGroup.BACK,
            )
        )
    }
}

