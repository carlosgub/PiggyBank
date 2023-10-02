package model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FinanceScreenModel(
    val localDateTime: LocalDateTime,
    val expenseAmount: Int,
    val monthExpense: MonthExpense,
    val expenses: List<FinanceScreenExpenses>,
    val income: List<FinanceScreenExpenses>
)

@Serializable
data class FinanceScreenExpenses(
    val category: CategoryEnum,
    val amount: Int,
    val count: Int,
    val percentage: Int
)

@Serializable
data class MonthExpense(
    val incomeTotal: Double,
    val percentage: Int
)
