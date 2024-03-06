package org.kepocnhh.thewolf.util

import org.kepocnhh.thewolf.entity.YMD
import java.util.Calendar
import java.util.TimeZone
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal fun calendarOfToday(
    hours: Int,
    minutes: Int,
    milliseconds: Int,
): Calendar {
    val calendar = Calendar.getInstance()
    calendar[Calendar.HOUR] = hours
    calendar[Calendar.MINUTE] = minutes
    calendar[Calendar.MILLISECOND] = milliseconds
    return calendar
}

internal fun calendarOf(
    dateTime: Duration,
): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateTime.inWholeMilliseconds
    return calendar
}

internal fun Calendar.toYMD(): YMD {
    return YMD(
        year = this[Calendar.YEAR],
        month = this[Calendar.MONTH],
        day = this[Calendar.DAY_OF_MONTH],
    )
}

internal val Calendar.duration: Duration
    get() = timeInMillis.milliseconds
