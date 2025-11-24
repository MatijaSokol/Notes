package com.matijasokol.notes.date

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

val dateTimeFormat = LocalDateTime.Format {
    day()
    char('.')
    monthNumber()
    char('.')
    year()
    char('.')

    char(' ')

    hour()
    char(':')
    minute()
}

fun timestampToDateFormatted(
    timestamp: Long,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): String = Instant.fromEpochMilliseconds(timestamp)
    .toLocalDateTime(timeZone)
    .format(dateTimeFormat)

fun millisNow(
    dayOffset: Int = 0,
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): Long = clock.now()
    .plus(dayOffset, DateTimeUnit.DAY, timeZone)
    .toEpochMilliseconds()
