package utils

import domain.model.CategoryEnum
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

fun getCurrentMonthKey(): String {
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val year = today.year
    val month = today.month.toMonthString()
    return "${month}$year"
}

fun getCategoryEnumFromName(name: String): CategoryEnum {
    for (enum in CategoryEnum.entries) {
        if (enum.name == name) {
            return enum
        }
    }
    return CategoryEnum.entries.first()
}
