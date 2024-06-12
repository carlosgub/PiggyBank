package com.carlosgub.myfinances.core.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

fun getCurrentMonthKey(): String {
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val year = today.year
    val month = today.month.toMonthString()
    return "${month}$year"
}

fun createLocalDateTime(
    year: Int,
    monthNumber: Int,
    dayOfMonth: Int = 1,
) = LocalDateTime(
    year = year,
    monthNumber = monthNumber,
    dayOfMonth = dayOfMonth,
    hour = 0,
    minute = 0,
    second = 0,
    nanosecond = 0,
)
