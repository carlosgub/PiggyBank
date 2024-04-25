package utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import model.CategoryEnum
import model.FinanceEnum

fun getCurrentMonthKey(): String {
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val year = today.year
    val month = today.month.toMonthString()
    return "${month}$year"
}

fun getCategoryEnumFromName(name: String): CategoryEnum {
    for (enum in CategoryEnum.values()) {
        if (enum.name == name) {
            return enum
        }
    }
    return CategoryEnum.entries.first()
}

fun getFinanceEnumFromName(name: String): FinanceEnum {
    for (enum in FinanceEnum.values()) {
        if (enum.name == name) {
            return enum
        }
    }
    return FinanceEnum.entries.first()
}
