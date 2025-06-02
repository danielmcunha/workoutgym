package com.southapps.data.workout

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.southapps.data.user.UserRepositoryImpl
import com.southapps.domain.workout.entities.Workout
import com.southapps.domain.workout.entities.WorkoutSummary
import com.southapps.domain.workout.entities.WorkoutHistory
import com.southapps.domain.workout.entities.WorkoutProgress
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WorkoutRepositoryImpl(
    private val prefs: DataStore<Preferences>
) : WorkoutRepository {

    override suspend fun getWorkouts(chickenId: String?): List<Workout> {
        val userId = chickenId ?: Firebase.auth.currentUser?.uid.orEmpty()
        val documents = Firebase.firestore
            .collection(UserRepositoryImpl.COLLECTION_PATH)
            .document(userId)
            .collection(WORKOUTS_COLLECTION_PATH)
            .get()
            .documents

        return documents.map { doc ->
            doc.data(Workout.serializer()).copy(uid = doc.id)
        }
    }

    override suspend fun getWorkoutFromPath(path: String): Workout {
        return Firebase.firestore.document(path).get().data()
    }

    override suspend fun createOrEdit(workout: Workout, chickenId: String?) {
        val currentUserId = Firebase.auth.currentUser?.uid.orEmpty()
        val userId = chickenId ?: currentUserId

        try {
            Firebase.firestore.runTransaction {
                val workoutRef = Firebase.firestore.collection(UserRepositoryImpl.COLLECTION_PATH)
                    .document(userId)
                    .collection(WORKOUTS_COLLECTION_PATH)
                    .document(workout.uid)

                workoutRef.set(workout)

                if (chickenId != null) {
                    Firebase.firestore
                        .collection(UserRepositoryImpl.COLLECTION_PATH)
                        .document(Firebase.auth.currentUser?.uid.orEmpty())
                        .collection(CHICKENS_COLLECTION_PATH)
                        .document(chickenId)
                        .collection(WORKOUTS_COLLECTION_PATH)
                        .document(workout.uid)
                        .set(
                            WorkoutSummary(
                                workoutReference = workoutRef.path,
                                order = workout.order,
                                name = workout.name,
                                workoutGoal = workout.goal,
                            )
                        )
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun saveHistory(workoutHistory: WorkoutHistory) {
        val data = Json.encodeToString(workoutHistory)
        prefs.edit {
            it[getPreferencesKey(workoutHistory.name)] = data
        }
    }

    override suspend fun getHistory(name: String): WorkoutHistory? {
        val preferences = prefs.data.first()
        val data = preferences[getPreferencesKey(name)]
        return data?.let {
            Json.decodeFromString<WorkoutHistory>(it)
        }
    }

    override suspend fun saveCurrentWorkout(workoutProgress: WorkoutProgress) {
        val data = Json.encodeToString(workoutProgress)
        prefs.edit {
            it[getPreferencesKey(CURRENT_TRAINING)] = data
        }
    }

    override suspend fun getCurrentWorkout(): WorkoutProgress? {
        val preferences = prefs.data.first()
        val data = preferences[getPreferencesKey(CURRENT_TRAINING)]
        return data?.let {
            Json.decodeFromString<WorkoutProgress>(it)
        }
    }

    override suspend fun deleteCurrentWorkout(id: String) {
        prefs.edit {
            it.remove(getPreferencesKey(CURRENT_TRAINING))
        }
    }

    private fun getPreferencesKey(name: String) = stringPreferencesKey(
        TRAINING_HISTORY + name.replace(" ", "_")
    )

    companion object {
        const val WORKOUTS_COLLECTION_PATH = "workouts"
        const val CHICKENS_COLLECTION_PATH = "chickens"

        private const val TRAINING_HISTORY = "workout_history_"
        private const val CURRENT_TRAINING = "current_workout"
    }
}