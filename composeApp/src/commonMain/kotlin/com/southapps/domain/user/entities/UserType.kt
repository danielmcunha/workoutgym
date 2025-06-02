package com.southapps.domain.user.entities

import kotlinx.serialization.Serializable

@Serializable
enum class UserType(value: Int) {
    Chicken(0),
    Master(1)
}
