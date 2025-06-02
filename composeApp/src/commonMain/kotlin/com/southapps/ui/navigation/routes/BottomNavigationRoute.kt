package com.southapps.ui.navigation.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

data class BottomNavigationData(
    val route: Any,
    val label: String,
    val icon: ImageVector
)

object BottomNavigationRoute {

    @Serializable
    object Home

    @Serializable
    object Profile

    @Serializable
    object Settings

    val items = listOf(
        BottomNavigationData(Home, "Início", Icons.Default.Home),
        BottomNavigationData(Profile, "Perfil", Icons.Default.Person),
        BottomNavigationData(Settings, "Configurações", Icons.Default.Settings)
    )
    val routes = items.map { it.route }
}