package model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FinanceScreenModel(
    val localDateTime: LocalDateTime,
    val expenseAmount: Long,
    val monthExpense: MonthExpense,
    val expenses: List<FinanceScreenExpenses>,
    val income: List<FinanceScreenExpenses>,
    val daySpent: Map<LocalDateTime, Long>
)

@Serializable
data class FinanceScreenExpenses(
    val category: CategoryEnum,
    val amount: Long,
    val count: Int,
    val percentage: Int
)

@Serializable
data class MonthExpense(
    val incomeTotal: Double,
    val percentage: Long
)
