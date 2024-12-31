package com.matijasokol.notes.date

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

val dateTimeFormat = LocalDateTime.Format {
    dayOfMonth()
    char('.')
    monthNumber(padding = Padding.SPACE)
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
