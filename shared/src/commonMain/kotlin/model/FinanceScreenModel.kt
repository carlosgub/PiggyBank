package model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

data class FinanceScreenModel(
    val month: Month = Month.JANUARY,
    val expenseAmount: Long = 0L,
    val monthExpense: MonthExpense = MonthExpense(),
    val expenses: List<FinanceScreenExpenses> = listOf(),
    val income: List<FinanceScreenExpenses> = listOf(),
    val daySpent: Map<LocalDateTime, Long> = mapOf()
)

data class FinanceScreenExpenses(
    val category: CategoryEnum,
    val amount: Long,
    val count: Int,
    val percentage: Int
)

data class MonthExpense(
    val incomeTotal: Double = 0.0,
    val percentage: Long = 0L
)
