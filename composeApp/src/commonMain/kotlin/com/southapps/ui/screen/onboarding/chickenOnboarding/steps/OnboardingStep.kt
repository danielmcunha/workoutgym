package com.southapps.ui.screen.onboarding.chickenOnboarding.steps

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class OnboardingStep(
    open val title: String,
    open var value: String? = null,
    open val actionTitle: String = "Próximo",
    open val slugName: String = "",
    open val action: (String) -> Unit,
) {
    data class InformativeStep(
        val icon: ImageVector,
        val description: String,
        override val title: String,
        override var value: String?,
        override val actionTitle: String = "Começar",
        override val slugName: String = "",
        override val action: (String) -> Unit,
    ) : OnboardingStep(
        title = title,
        value = value,
        actionTitle = actionTitle,
        slugName = slugName,
        action = action
    )

    data class SelectStep(
        val options: List<String>,
        val allowMultiples: Boolean = false,
        override val title: String,
        override var value: String?,
        override val actionTitle: String = "Próximo",
        override val slugName: String = "",
        override val action: (String) -> Unit,
    ) : OnboardingStep(
        title = title,
        value = value,
        actionTitle = actionTitle,
        slugName = slugName,
        action = action
    )

    data class DatePickerStep(
        override val title: String,
        override var value: String?,
        override val actionTitle: String = "Próximo",
        override val slugName: String = "",
        override val action: (String) -> Unit,
    ) : OnboardingStep(
        title = title,
        value = value,
        actionTitle = actionTitle,
        slugName = slugName,
        action = action
    )

    data class InputStep(
        val inputs: List<Input>,
        override val title: String,
        override var value: String?,
        override val actionTitle: String = "Próximo",
        override val slugName: String = "",
        override val action: (String) -> Unit,
    ) : OnboardingStep(
        title = title,
        value = value,
        actionTitle = actionTitle,
        slugName = slugName,
        action = action
    ) {
        abstract class Input(
            open val title: String,
            open var value: String,
        )

        data class NumberInput(
            override val title: String,
            override var value: String,
            val decimals: Int = 2,
            val step: Float = 1f,
            val suffix: String = "",
        ) : Input(title, value)

        fun getInputsValue(): String {
            val map = inputs.map {
                it.title to it.value
            }.toMap()

            return Json.encodeToString(map)
        }

        fun updateInputValue(title: String, value: String) : String {
            inputs.firstOrNull { it.title == title }?.value = value

            return getInputsValue()
        }

    }
}



