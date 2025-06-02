package com.southapps.domain.chicken.entities

import com.southapps.core.extensions.currentTime
import com.southapps.core.extensions.timeInMillis
import com.southapps.data.user.UserRepositoryImpl
import com.southapps.domain.user.entities.User
import com.southapps.domain.user.entities.UserBodyInfo
import com.southapps.domain.user.entities.UserGoals
import com.southapps.domain.workout.entities.WorkoutSummary
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ChickenSummary(
    val name: String,
    val email: String,
    val age: Int? = null,
    val userId: String,
    val userReference: String,
    val gender: String? = null,
    val goal: UserGoals? = null,
    val phone: String? = null,
    val note: String? = null,
    val bodyInfo: UserBodyInfo? = null,
    val createdAt: Long = currentTime().timeInMillis(),
    @Transient
    val workouts: List<WorkoutSummary> = emptyList(),
) {

    companion object {
        fun User.toChickenSummary() = ChickenSummary(
            name = name.orEmpty(),
            email = email.orEmpty(),
            age = 17, // calculate
            userId = uid.orEmpty(),
            userReference = "/${UserRepositoryImpl.COLLECTION_PATH}/${uid.orEmpty()}",
            gender = gender,
            goal = goals,
            phone = phone,
            note = null,
            bodyInfo = bodyInfo
        )
    }

}