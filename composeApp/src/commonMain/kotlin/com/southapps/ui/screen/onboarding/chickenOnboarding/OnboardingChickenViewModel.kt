package com.southapps.ui.screen.onboarding.chickenOnboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.southapps.core.BaseViewModel
import com.southapps.core.UIAction
import com.southapps.core.UIState
import com.southapps.data.user.UserRepositoryImpl
import com.southapps.domain.common.useCase.Resource
import com.southapps.domain.user.entities.User
import com.southapps.domain.user.entities.UserBodyInfo
import com.southapps.domain.user.entities.UserGoals
import com.southapps.domain.user.entities.UserType
import com.southapps.domain.user.useCases.UserEditUseCase
import com.southapps.ui.navigation.Navigator
import com.southapps.ui.navigation.routes.BottomNavigationRoute
import com.southapps.ui.screen.onboarding.chickenOnboarding.steps.OnboardingStep
import dev.gitlive.firebase.firestore.Direction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KClass

private enum class StepName(name: String) {
    MainGoal("main-goal"),
    BornDate("born-date"),
    Gender("gender"),
    BodyInfo("body-info"),
    Experience("experience"),
    WeeklyFrequency("weekly-frequency"),
}

data class OnboardingChickenViewState(
    val stepIndex: Int = 0,
    val steps: List<OnboardingStep>,
    val direction: Direction = Direction.ASCENDING,
    val isLoading: Boolean = false
) : UIState {
    val stepCount: Int
        get() = steps.size

    val step: OnboardingStep
        get() = steps[stepIndex]

    fun updateCurrentStepValue(value: String) = this.copy(
        steps = steps.apply {
            this[stepIndex].value = value
        }
    )

    fun previousStep() = this.copy(
        stepIndex = max(0, stepIndex - 1),
        direction = Direction.DESCENDING
    )

    fun nextStep() = this.copy(
        stepIndex = min(stepIndex + 1, steps.size - 1),
        direction = Direction.ASCENDING
    )
}

class OnboardingChickenViewModel(
    navigator: Navigator,
    private val userEditUseCase: UserEditUseCase,
) :
    BaseViewModel<OnboardingChickenViewState, UIAction>(navigator) {

    private val _uiState = MutableStateFlow(
        OnboardingChickenViewState(
            steps = listOf(
                OnboardingStep.SelectStep(
                    title = "Objetivo",
                    value = null,
                    options = listOf(
                        "Ganho de massa muscular",
                        "Emagrecimento",
                        "Condicionamento físico",
                        "Qualidade de vida"
                    ),
                    allowMultiples = true,
                    slugName = StepName.MainGoal.name,
                    action = ::updateValue
                ),
                OnboardingStep.DatePickerStep(
                    "Nascimento",
                    value = null,
                    slugName = StepName.BornDate.name,
                    action = ::updateValue
                ),
                OnboardingStep.SelectStep(
                    title = "Gênero",
                    value = null,
                    options = listOf(
                        "Feminino",
                        "Masculino",
                        "Não informar"
                    ),
                    slugName = StepName.Gender.name,
                    action = ::updateValue
                ),
                OnboardingStep.InputStep(
                    title = "Medidas Corporais",
                    inputs = listOf(
                        OnboardingStep.InputStep.NumberInput(
                            title = "Altura",
                            value = "1.70",
                            suffix = "m",
                            step = 0.01f
                        ),
                        OnboardingStep.InputStep.NumberInput(
                            title = "Peso",
                            value = "70",
                            decimals = 1,
                            suffix = "kg",
                            step = 1f
                        ),
                    ),
                    value = null,
                    slugName = StepName.BodyInfo.name,
                    action = ::updateValue
                ),
                OnboardingStep.SelectStep(
                    title = "Experiência",
                    value = "",
                    options = listOf(
                        "Nunca treinei antes",
                        "0 a 1 ano",
                        "1 a 3 anos",
                        "3 a 10 anos",
                        "mais de 10 anos",
                    ),
                    slugName = StepName.Experience.name,
                    action = ::updateValue
                ),
                OnboardingStep.SelectStep(
                    title = "Objetivo semanal",
                    value = "",
                    options = listOf(
                        "1 vez na semana",
                        "2 vezes na semana",
                        "3 vezes na semana",
                        "4 vezes na semana",
                        "5 vezes na semana",
                        "6 vezes na semana",
                        "todos os dias",
                    ),
                    slugName = StepName.WeeklyFrequency.name,
                    action = ::updateValue
                ),
                OnboardingStep.InformativeStep(
                    icon = Icons.Default.Check,
                    description = "Que ótimo, estamos prontos para começar!",
                    title = "Tudo pronto",
                    value = null,
                    action = ::updateValue
                )
            ),
        )
    )
    override val uiState: StateFlow<OnboardingChickenViewState> = _uiState

    fun updateValue(value: String) {
        _uiState.update {
            it.updateCurrentStepValue(value = value)
        }
    }

    fun previous() {
        _uiState.update {
            it.previousStep()
        }
    }

    fun next() {
        _uiState.value.let { state ->
            if (state.stepIndex == state.steps.size - 1) {
                updateUser(state.steps)

                return
            }
        }

        _uiState.update { it.nextStep() }
    }

    fun jump() {
        sendAction(
            UIAction.ShowDialog(
                "Pular onboarding",
                "Puxa, que pena :/ \nVocê tem certeza que deseja pular o onboarding",
                Pair("Não") { },
                Pair("Sim") {
                    navigator.navigate(BottomNavigationRoute.Home)
                }
            )
        )

    }

    private fun setLoading(loading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = loading)
    }

    private fun updateUser(steps: List<OnboardingStep>) {
        val bornDate = steps.firstOrNull { it.slugName == StepName.BornDate.name }?.value.orEmpty()
        val gender = steps.firstOrNull { it.slugName == StepName.Gender.name }?.value.orEmpty()

        val mainGoal = steps.firstOrNull { it.slugName == StepName.MainGoal.name }?.value.orEmpty()
        val experience =
            steps.firstOrNull { it.slugName == StepName.Experience.name }?.value.orEmpty()
        val weeklyFrequency =
            (steps.firstOrNull { it.slugName == StepName.WeeklyFrequency.name } as? OnboardingStep.SelectStep)?.run {
                (this.options.indexOf(this.value) + 1).toString()
            }.orEmpty()

        val bodyInfo =
            (steps.firstOrNull { it.slugName == StepName.BodyInfo.name } as? OnboardingStep.InputStep)?.run {
                UserBodyInfo(
                    height = this.inputs.firstOrNull {
                        it.title.equals(
                            "altura",
                            true
                        )
                    }?.value.orEmpty(),
                    weight = this.inputs.firstOrNull {
                        it.title.equals(
                            "peso",
                            true
                        )
                    }?.value.orEmpty(),
                )
            }

        val user = User(
            name = "",
            email = "",
            type = UserType.Chicken,
            bornDate = bornDate,
            gender = gender,
            bodyInfo = bodyInfo,
            goals = UserGoals(
                mainGoal = mainGoal,
                experience = experience,
                weeklyFrequency = weeklyFrequency
            )
        )

        setLoading(true)
        viewModelScope.launch {
            userEditUseCase.execute(user).collect {
                when (it) {
                    is Resource.Error<*> -> {
                        setLoading(false)
                    }

                    is Resource.Success<*> -> navigator.navigate(BottomNavigationRoute.Home)
                }
            }
        }
    }
}