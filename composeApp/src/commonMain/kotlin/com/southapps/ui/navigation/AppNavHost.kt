package com.southapps.ui.navigation

import WorkoutScheduleScreen
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.southapps.data.session.UserSession
import com.southapps.domain.workout.entities.WorkoutProgress
import com.southapps.domain.workout.entities.fakeWorkout
import com.southapps.ui.navigation.component.ComponentInjectorImpl
import com.southapps.ui.navigation.routes.BaseRoute
import com.southapps.ui.navigation.routes.BottomNavigationRoute
import com.southapps.ui.navigation.routes.MasterRoute
import com.southapps.ui.navigation.routes.OnboardingRoute
import com.southapps.ui.navigation.routes.WorkoutRoute
import com.southapps.ui.screen.associate.master.listInvites.AssociateListInvitesScreen
import com.southapps.ui.screen.associate.master.sendInvite.AssociateSendInviteScreen
import com.southapps.ui.screen.home.HomeScreen
import com.southapps.ui.screen.login.LoginScreen
import com.southapps.ui.screen.master.chicken.detail.ChickenDetailScreen
import com.southapps.ui.screen.master.chicken.list.ChickenListScreen
import com.southapps.ui.screen.onboarding.chickenOnboarding.OnboardingChickenScreen
import com.southapps.ui.screen.onboarding.selectYourJourney.OnboardingSelectYourJourneyScreen
import com.southapps.ui.screen.onboarding.welcome.OnboardingWelcomeScreen
import com.southapps.ui.screen.profile.ProfileScreen
import com.southapps.ui.screen.profile.manageMaster.ManageMasterScreen
import com.southapps.ui.screen.register.RegisterScreen
import com.southapps.ui.screen.routine.create.RoutineCreateScreen
import com.southapps.ui.screen.workout.SharedWorkoutViewModel
import com.southapps.ui.screen.workout.active.WorkoutScreen
import com.southapps.ui.screen.workout.completed.WorkoutSuccessScreen
import com.southapps.ui.screen.workout.create.WorkoutCreateScreen
import com.southapps.ui.screen.workout.exercise.add.WorkoutAddExerciseScreen
import com.southapps.ui.screen.workout.exercise.list.WorkoutExerciseListScreen
import com.southapps.ui.screen.workout.report.WorkoutReportScreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    navigator: Navigator = koinInject(),
    modifier: Modifier = Modifier,
    userSession: UserSession = koinInject(),
) {
    val componentInjector = remember { ComponentInjectorImpl() }

    LaunchedEffect(navigator) {
        navigator.route.collect {
            if (it.popBack) {
                navController.popBackStack()
            }

            it.route?.let { route ->
                navController.navigate(route) {
                    if (it.clearStack) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = navigator.startDestination,
            modifier = modifier
        ) {
            // region App home

            composable<BottomNavigationRoute.Home> { backStackEntry ->
                val coroutineScope = rememberCoroutineScope()

                Column {
                    Button(onClick = {
                        coroutineScope.launch {
                            navigator.navigate(WorkoutRoute.WorkoutCreate())
                        }
                    }) {
                        Text("Create workout")
                    }

                    Button(onClick = {
                        coroutineScope.launch {
                            navigator.navigate(
                                WorkoutRoute.Active(
                                    WorkoutProgress.fromWorkout(fakeWorkout())
                                )
                            )
                        }
                    }) {
                        Text("Iniciar treino")
                    }

                    Button(onClick = {
                        coroutineScope.launch {
                            navigator.navigate(WorkoutRoute.Report)
                        }
                    }) {
                        Text("Relatório de treino")
                    }

                    if (userSession.isUserMaster()) {

                        Button(onClick = {
                            coroutineScope.launch {
                                navigator.navigate(MasterRoute.MasterChickenList)
                            }
                        }) {
                            Text("Ver alunos")
                        }

                        Button(onClick = {
                            coroutineScope.launch {
                                navigator.navigate(BaseRoute.AssociateSendInviteScreen)
                            }
                        }) {
                            Text("Associar frango")
                        }

                        Button(onClick = {
                            coroutineScope.launch {
                                navigator.navigate(BaseRoute.AssociateListInvitesScreen)
                            }
                        }) {
                            Text("Convites abertos")
                        }

                    }

                    HomeScreen(componentInjector)
                }
            }

            composable<BottomNavigationRoute.Profile> { ProfileScreen(animatedVisibilityScope = this) }

            composable<BottomNavigationRoute.Settings> { Text("Settings") }

            // endregion App home

            // region Master

            composable<BaseRoute.MasterPlans> { Text("MasterPlans") }

            composable<BaseRoute.UserManageMaster> {
                ManageMasterScreen(animatedVisibilityScope = this)
            }
            // endregion

            // region Login and Register

            composable<BaseRoute.Login> {
                LoginScreen()
            }

            composable<BaseRoute.Register> {
                RegisterScreen()
            }

            // endregion

            // region OnBoarding
            navigation<OnboardingRoute.Onboarding>(
                startDestination = OnboardingRoute.Welcome
            ) {
                composable<OnboardingRoute.Welcome> {
                    OnboardingWelcomeScreen()
                }

                composable<OnboardingRoute.SelectYourJourney> {
                    OnboardingSelectYourJourneyScreen()
                }

                composable<OnboardingRoute.ChickenOnboarding> {
                    OnboardingChickenScreen()
                }
            }

            // endregion OnBoarding

            // region Workout
            navigation<WorkoutRoute.Workout>(
                startDestination = WorkoutRoute.WorkoutCreate()
            ) {
                composable<WorkoutRoute.WorkoutCreate> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(WorkoutRoute.Workout)
                    }

                    val sharedWorkoutViewModel =
                        koinViewModel<SharedWorkoutViewModel>(viewModelStoreOwner = parentEntry)

                    WorkoutCreateScreen(
                        sharedViewModel = sharedWorkoutViewModel
                    )
                }

                composable<WorkoutRoute.WorkoutAddExercise> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(WorkoutRoute.Workout)
                    }

                    WorkoutAddExerciseScreen(
                        sharedViewModel = koinViewModel<SharedWorkoutViewModel>(viewModelStoreOwner = parentEntry)
                    )
                }

                composable<WorkoutRoute.RoutineManager> { backStackEntry ->
                    WorkoutScheduleScreen()
                }

                composable<WorkoutRoute.RoutineCreate> { backStackEntry ->
                    RoutineCreateScreen()
                }

                composable<WorkoutRoute.WorkoutExerciseList> { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(WorkoutRoute.Workout)
                    }

                    WorkoutExerciseListScreen(
                        sharedViewModel = koinViewModel<SharedWorkoutViewModel>(viewModelStoreOwner = parentEntry)
                    )
                }
            }

            // endregion Workout

            // region Workout

            composable<WorkoutRoute.Active> {
                WorkoutScreen()
            }

            composable<WorkoutRoute.Report> {
                WorkoutReportScreen()
            }

            composable<WorkoutRoute.Completed> {
                WorkoutSuccessScreen()
            }

            // endregion

            // region Master Chickens

            composable<MasterRoute.MasterChickenList> {
                ChickenListScreen(
                    animatedVisibilityScope = this
                )
            }

            composable<MasterRoute.MasterChickenDetail> { backStackEntry ->
                ChickenDetailScreen(
                    animatedVisibilityScope = this
                )
            }

            // endregion

            // region Associate

            composable<BaseRoute.AssociateSendInviteScreen> {
                AssociateSendInviteScreen()
            }

            composable<BaseRoute.AssociateListInvitesScreen> {
                AssociateListInvitesScreen()
            }

            // endregion
        }
    }
}
