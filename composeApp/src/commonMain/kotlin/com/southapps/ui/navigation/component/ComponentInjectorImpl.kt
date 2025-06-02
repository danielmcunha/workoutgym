package com.southapps.ui.navigation.component

import androidx.compose.runtime.Composable

class ComponentInjectorImpl : ComponentInjector {

    @Composable
    override fun WorkoutCurrentComponent() {
        com.southapps.ui.screen.workout.current.WorkoutCurrentComponent()
    }

    @Composable
    override fun AssociateCheckInviteComponent() {
        com.southapps.ui.screen.associate.chicken.checkInvite.AssociateCheckInviteComponent()
    }
}
