package com.southapps.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.southapps.ui.navigation.component.ComponentInjector

@Composable
fun HomeScreen(componentInjector: ComponentInjector) {
    Column(modifier = Modifier.fillMaxSize()) {
        componentInjector.AssociateCheckInviteComponent()

        componentInjector.WorkoutCurrentComponent()

    }
}