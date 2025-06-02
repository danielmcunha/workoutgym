package com.southapps.core.di

import WorkoutRoutineManagerViewModel
import com.southapps.AppEntryViewModel
import com.southapps.ui.screen.associate.master.sendInvite.AssociateSendInviteViewModel
import com.southapps.ui.screen.associate.master.remove.AssociateRemoveChickenViewModel
import com.southapps.ui.screen.associate.master.listInvites.AssociateListInvitesViewModel
import com.southapps.ui.screen.associate.chicken.checkInvite.AssociateCheckInviteViewModel
import com.southapps.ui.screen.associate.chicken.remove.AssociateRemoveMasterViewModel
import com.southapps.ui.screen.login.LoginViewModel
import com.southapps.ui.screen.master.chicken.detail.ChickenDetailViewModel
import com.southapps.ui.screen.master.chicken.list.ChickenListViewModel
import com.southapps.ui.screen.onboarding.chickenOnboarding.OnboardingChickenViewModel
import com.southapps.ui.screen.onboarding.selectYourJourney.OnboardingSelectYourJourneyViewModel
import com.southapps.ui.screen.onboarding.welcome.OnboardingWelcomeViewModel
import com.southapps.ui.screen.register.RegisterViewModel
import com.southapps.ui.screen.workout.SharedWorkoutViewModel
import com.southapps.ui.screen.workout.active.WorkoutActiveViewModel
import com.southapps.ui.screen.workout.completed.WorkoutSuccessViewModel
import com.southapps.ui.screen.workout.create.WorkoutCreateViewModel
import com.southapps.ui.screen.workout.current.WorkoutCurrentComponentViewModel
import com.southapps.ui.screen.workout.exercise.add.WorkoutAddExerciseViewModel
import com.southapps.ui.screen.workout.exercise.list.WorkoutExerciseListViewModel
import com.southapps.ui.screen.routine.create.RoutineCreateViewModel
import com.southapps.ui.screen.workout.report.WorkoutReportViewModel
import com.southapps.ui.screen.profile.ProfileViewModel
import com.southapps.ui.screen.profile.manageMaster.ManageMasterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::ChickenListViewModel)
    viewModelOf(::ChickenDetailViewModel)
    viewModelOf(::OnboardingChickenViewModel)
    viewModelOf(::OnboardingSelectYourJourneyViewModel)
    viewModelOf(::OnboardingWelcomeViewModel)
    viewModelOf(::WorkoutActiveViewModel)
    viewModelOf(::WorkoutSuccessViewModel)
    viewModelOf(::WorkoutCreateViewModel)
    viewModelOf(::WorkoutCurrentComponentViewModel)
    viewModelOf(::WorkoutAddExerciseViewModel)
    viewModelOf(::WorkoutExerciseListViewModel)
    viewModelOf(::WorkoutReportViewModel)
    viewModelOf(::SharedWorkoutViewModel)
    viewModelOf(::AppEntryViewModel)
    viewModelOf(::WorkoutRoutineManagerViewModel)
    viewModelOf(::RoutineCreateViewModel)
    viewModelOf(::AssociateCheckInviteViewModel)
    viewModelOf(::AssociateSendInviteViewModel)
    viewModelOf(::AssociateListInvitesViewModel)
    viewModelOf(::AssociateRemoveMasterViewModel)
    viewModelOf(::AssociateRemoveChickenViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ManageMasterViewModel)
}