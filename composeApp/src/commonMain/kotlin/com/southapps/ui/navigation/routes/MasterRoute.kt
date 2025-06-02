package com.southapps.ui.navigation.routes

import kotlinx.serialization.Serializable

object MasterRoute {

    @Serializable data object MasterChickenList
    @Serializable data class MasterChickenDetail(val chickenId:String, val chickenPath: String)
}