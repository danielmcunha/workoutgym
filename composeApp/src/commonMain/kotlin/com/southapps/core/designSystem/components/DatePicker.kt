package com.southapps.core.designSystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

import kotlinx.datetime.*

private fun getDaysInMonth(year: Int, month: Int): Int {
    val firstDayOfMonth = LocalDate(year, month, 1)
    val firstDayOfNextMonth = firstDayOfMonth.plus(1, DateTimeUnit.MONTH)
    return firstDayOfNextMonth.minus(1, DateTimeUnit.DAY).dayOfMonth
}

@Composable
fun DatePicker(date: LocalDate? = null, onDataChange: (String) -> Unit) {

    val days = (1..31).toList()
    val months = listOf(
        "Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
        "Jul", "Ago", "Set", "Out", "Nov", "Dez"
    )

    val currentDate: LocalDate =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    val first = currentDate.year - 112
    val lastYear = currentDate.year - 18

    val years = (first..lastYear).toList()

    var selectedDay by remember {
        mutableStateOf(
            date?.dayOfMonth ?: currentDate.dayOfMonth
        )
    }
    var selectedMonth by remember {
        mutableStateOf(
            (date?.monthNumber ?: (currentDate.monthNumber)) - 1
        )
    }
    var selectedYear by remember {
        mutableStateOf(
            date?.year ?: lastYear
        )
    }

    val buildData: () -> String = {
        val month = (selectedMonth + 1).toString().padStart(2, '0')
        "$selectedYear-$month-$selectedDay"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        VerticalSnapPicker(
            title = "Dia",
            items = days,
            selectedItem = selectedDay,
            onItemSelected = {
                if(selectedDay == it) return@VerticalSnapPicker
                if (it <= getDaysInMonth(selectedYear, (selectedMonth) + 1)) {
                    selectedDay = it
                }

                onDataChange(buildData())
            }
        )

        VerticalSnapPicker(
            title = "Mês",
            items = months,
            selectedItem = months[selectedMonth],
            onItemSelected = {
                if(selectedMonth == months.indexOf(it)) return@VerticalSnapPicker
                selectedMonth = months.indexOf(it)
                onDataChange(buildData())
            }
        )

        VerticalSnapPicker(
            title = "Ano",
            items = years,
            selectedItem = selectedYear,
            onItemSelected = {
                if(selectedYear == it) return@VerticalSnapPicker
                selectedYear = it
                onDataChange(buildData())
            }
        )
    }
}