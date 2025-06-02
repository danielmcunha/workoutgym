package com.southapps.core.di

import com.southapps.data.routine.RoutineRepository
import com.southapps.data.routine.RoutineRepositoryImpl
import com.southapps.data.user.UserRepository
import com.southapps.data.user.UserRepositoryImpl
import com.southapps.data.workout.WorkoutRepository
import com.southapps.data.workout.WorkoutRepositoryImpl
import com.southapps.data.associate.AssociateRepository
import com.southapps.data.associate.AssociateRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::WorkoutRepositoryImpl) { bind<WorkoutRepository>() }
    singleOf(::RoutineRepositoryImpl) { bind<RoutineRepository>() }
    singleOf(::AssociateRepositoryImpl) { bind<AssociateRepository>() }
}