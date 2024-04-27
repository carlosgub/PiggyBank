package domain.model

import kotlinx.datetime.LocalDate
import utils.createLocalDateTime
import utils.toNumberOfTwoDigits

data class FinanceLocalDate(
    val localDate: LocalDate
) {
    val localDateTime = createLocalDateTime(
        year = localDate.year,
        monthNumber = localDate.monthNumber,
        dayOfMonth = localDate.dayOfMonth
    )
    private val dayOfMonth = localDateTime.dayOfMonth.toNumberOfTwoDigits()
    private val month = localDateTime.month.name.lowercase()
        .replaceFirstChar { it.uppercase() }
    private val year = localDateTime.year

    val date = "$dayOfMonth $month $year"
}
