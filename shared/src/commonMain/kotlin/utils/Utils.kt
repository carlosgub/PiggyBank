package utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import model.CategoryEnum

fun getCurrentMonthKey(): String {
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val year = today.year
    val month = today.month.toMonthString()
    return "${month}$year"
}

fun getCurrentMonthName(): String = Clock.System.todayIn(TimeZone.currentSystemDefault()).month.name

fun getCategoryEnumFromName(name: String): CategoryEnum {
    for (enum in CategoryEnum.values()) {
        if (enum.name == name) {
            return enum
        }
    }
    return CategoryEnum.entries.first()
}
