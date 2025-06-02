package com.southapps.ui.screen.onboarding.chickenOnboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.southapps.BackHandler
import com.southapps.core.LocalUIActionRegister
import com.southapps.core.designSystem.components.CustomButton
import com.southapps.core.designSystem.components.DatePicker
import com.southapps.core.designSystem.components.InputStepperNumber
import com.southapps.core.designSystem.components.Stepper
import com.southapps.core.designSystem.tokens.Spacing
import com.southapps.ui.screen.onboarding.chickenOnboarding.steps.OnboardingStep
import dev.gitlive.firebase.firestore.Direction
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingChickenScreen(
    viewModel: OnboardingChickenViewModel = koinViewModel()
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(Spacing.lg),
    horizontalAlignment = Alignment.Start
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val register = LocalUIActionRegister.current

    DisposableEffect(Unit) {
        register(viewModel.uiAction)
        onDispose { }
    }

    BackHandler(uiState.stepIndex > 0, viewModel::previous)

    Spacer(Modifier.height(Spacing.xl))

    AnimatedContent(
        targetState = uiState.stepIndex,
        transitionSpec = {
            val dirMultiplier = if (uiState.direction == Direction.ASCENDING) 1 else -1

            slideInHorizontally { width -> width * dirMultiplier } + fadeIn() togetherWith
                    slideOutHorizontally { width -> -width * dirMultiplier } + fadeOut()
        }
    ) { _ ->
        Text(
            uiState.step.title,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Stepper(
        uiState.stepIndex, uiState.stepCount,
        modifier = Modifier.padding(
            horizontal = 0.dp,
            vertical = Spacing.sm
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        RenderStep(uiState.step)
    }

    CustomButton(
        uiState.step.actionTitle,
        onClick = { viewModel.next() },
        modifier = Modifier.fillMaxWidth(),
        loading = uiState.isLoading
    )

    Spacer(Modifier.height(Spacing.xs))

    Text(
        text = "Pular",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.jump()
            }
            .padding(Spacing.xs)
    )
}

@Composable
private fun RenderStep(step: OnboardingStep) {
    AnimatedContent(
        targetState = step,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    ) { currentStep ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (currentStep) {
                is OnboardingStep.DatePickerStep -> DateStep(currentStep)
                is OnboardingStep.InformativeStep -> InformativeStep(currentStep)
                is OnboardingStep.InputStep -> InputStep(currentStep)
                is OnboardingStep.SelectStep -> SelectStep(currentStep)
            }
        }
    }
}

@Composable
private fun InformativeStep(step: OnboardingStep.InformativeStep) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.xl)
    ) {

        Icon(
            step.icon, "",
            modifier = Modifier
                .size(120.dp)
        )

        Text(
            step.description,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun SelectStep(step: OnboardingStep.SelectStep) {
    var selectedOption by remember { mutableStateOf(step.value?.split(';')?.toList() ?: listOf()) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        step.options.forEach { option ->
            val isSelected = selectedOption.contains(option)

            val borderColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                    alpha = .7f
                ),
                label = "BorderColorAnimation"
            )

            OutlinedButton(
                onClick = {
                    val newValue = if (step.allowMultiples) {
                        if (selectedOption.contains(option)) {
                            selectedOption.filter { it != option }
                        } else {
                            selectedOption + option
                        }
                    } else {
                        listOf(option)
                    }

                    step.action(newValue.joinToString(separator = ";") { it })
                    selectedOption = newValue
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                border = BorderStroke(2.dp, borderColor),
                shape = MaterialTheme.shapes.extraSmall,
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(text = option, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}

@Composable
private fun DateStep(step: OnboardingStep.DatePickerStep) {
    val date = step.value?.let { LocalDate.parse(it) }
    DatePicker(date = date, onDataChange = { step.action(it) })
}

@Composable
private fun InputStep(step: OnboardingStep.InputStep) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        step.inputs.forEach { input ->
            when (input) {
                is OnboardingStep.InputStep.NumberInput -> InputStepperNumber(
                    title = input.title,
                    value = input.value,
                    suffix = input.suffix,
                    decimals = input.decimals,
                    step = input.step,
                    onChange = {
                        step.action(step.updateInputValue(input.title, it.toString()))
                    }
                )
            }
        }
    }
}