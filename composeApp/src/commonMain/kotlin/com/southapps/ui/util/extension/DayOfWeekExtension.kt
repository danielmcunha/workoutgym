package com.southapps.ui.util.extension

import kotlinx.datetime.DayOfWeek

fun DayOfWeek.getShortDisplayName(): String {
    return when (this) {
        DayOfWeek.MONDAY -> "Seg"
        DayOfWeek.TUESDAY -> "Ter"
        DayOfWeek.WEDNESDAY -> "Qua"
        DayOfWeek.THURSDAY -> "Qui"
        DayOfWeek.FRIDAY -> "Sex"
        DayOfWeek.SATURDAY -> "Sáb"
        DayOfWeek.SUNDAY -> "Dom"
        else -> ""
    }
}