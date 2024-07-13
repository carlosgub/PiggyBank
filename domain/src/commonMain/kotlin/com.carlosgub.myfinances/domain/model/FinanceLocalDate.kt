package com.carlosgub.myfinances.domain.model

import com.carlosgub.myfinances.core.utils.createLocalDateTime
import com.carlosgub.myfinances.core.utils.toNumberOfTwoDigits
import kotlinx.datetime.LocalDate

data class FinanceLocalDate(
    val localDate: LocalDate,
) {
    val localDateTime =
        createLocalDateTime(
            year = localDate.year,
            monthNumber = localDate.monthNumber,
            dayOfMonth = localDate.dayOfMonth,
        )
    private val dayOfMonth = localDateTime.dayOfMonth.toNumberOfTwoDigits()
    private val month =
        localDateTime.month.name.lowercase()
            .replaceFirstChar { it.uppercase() }
    private val year = localDateTime.year

    val date = "$dayOfMonth $month $year"
}
