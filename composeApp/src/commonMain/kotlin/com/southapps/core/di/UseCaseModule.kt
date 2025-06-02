package com.southapps.core.di

import com.southapps.domain.master.useCases.GetChickensUseCase
import com.southapps.domain.routine.useCases.RoutineCreateUseCase
import com.southapps.domain.routine.useCases.RoutineGetUseCase
import com.southapps.domain.routine.useCases.RoutineRemoveUseCase
import com.southapps.domain.user.useCases.*
import com.southapps.domain.workout.useCases.*
import com.southapps.domain.associate.useCases.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::GetChickensUseCase)
    factoryOf(::UserEditUseCase)
    factoryOf(::UserSendPasswordResetEmailUseCase)
    factoryOf(::UserSignInUseCase)
    factoryOf(::UserSignOutUseCase)
    factoryOf(::UserSignUpUseCase)
    factoryOf(::WorkoutCompleteUseCase)
    factoryOf(::WorkoutSaveUseCase)
    factoryOf(::WorkoutGetCurrentUseCase)
    factoryOf(::WorkoutDeleteCurrentUseCase)
    factoryOf(::WorkoutGetUseCase)
    factoryOf(::WorkoutReportUseCase)
    factoryOf(::WorkoutSaveCurrentUseCase)
    factoryOf(::LoadUserSessionUseCase)
    factoryOf(::AssociateCheckInviteUseCase)
    factoryOf(::AssociateSendInviteUseCase)
    factoryOf(::AssociateAcceptInviteUseCase)
    factoryOf(::AssociateDeclineInviteUseCase)
    factoryOf(::AssociateListInvitesUseCase)
    factoryOf(::AssociateCancelInviteUseCase)
    factoryOf(::AssociateChickenRemoveMasterUseCase)
    factoryOf(::AssociateMasterRemoveChickenUseCase)
    factoryOf(::RoutineCreateUseCase)
    factoryOf(::RoutineGetUseCase)
    factoryOf(::RoutineRemoveUseCase)
}