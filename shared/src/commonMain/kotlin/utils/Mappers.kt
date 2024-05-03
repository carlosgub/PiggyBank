package utils

import kotlinx.datetime.LocalDateTime

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
