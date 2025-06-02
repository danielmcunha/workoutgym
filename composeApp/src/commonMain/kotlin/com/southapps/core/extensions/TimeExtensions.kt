package com.southapps.core.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

fun Long.formatTime(): String {
    val secondsTotal = this / 1000

    val hours = secondsTotal / 3600
    val minutes = (secondsTotal % 3600) / 60
    val seconds = secondsTotal % 60

    val hoursStr = if (hours > 0) "$hours".padStart(2, '0') + ":" else ""
    val minutesStr = "$minutes".padStart(2, '0')
    val secondsStr = "$seconds".padStart(2, '0')

    return "$hoursStr:$minutesStr:$secondsStr"
}

fun Long.toDateLiteral(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = dateTime.dayOfMonth
    val month = dateTime.month.name.lowercase().take(3) // ex: "jan", "fev", etc.
    return "$day de $month"
}

fun Long.toDateLongLiteral(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = dateTime.dayOfMonth
    val month = dateTime.month.name.lowercase()
    val year = dateTime.year
    return "$day de $month de $year"
}

fun Long.toShortDateTime(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = "${dateTime.dayOfMonth}".padStart(2, '0')
    val month = "${dateTime.month.number}".padStart(2, '0')
    val hour = "${dateTime.hour}".padStart(2, '0')
    val minute = "${dateTime.minute}".padStart(2, '0')
    return "$day/$month $hour:$minute"
}

fun Long.toShortDate(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = dateTime.dayOfMonth
    val month = dateTime.month.number
    return "$day/$month"
}

fun String.dateToLongDate(): String {
    val parts = this.split("/")
    if (parts.size != 3) return ""

    val day = parts[0].padStart(2, '0')
    val month = parts[1].padStart(2, '0')
    val year = parts[2]

    return try {
        val date = LocalDate.parse("$year-$month-$day")
        val dateTime = date.atStartOfDayIn(TimeZone.currentSystemDefault())

        dateTime.toEpochMilliseconds().toDateLongLiteral()
    } catch (e: Exception) {
        ""
    }
}
