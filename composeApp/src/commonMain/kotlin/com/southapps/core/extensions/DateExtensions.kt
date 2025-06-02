package com.southapps.core.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun currentTime(): LocalDateTime =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.timeInMillis() = this.toInstant(
    TimeZone.of("America/Sao_Paulo")
).toEpochMilliseconds()