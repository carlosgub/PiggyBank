package com.carlosgub.myfinances.domain.model

import kotlinx.datetime.LocalDateTime

data class FinanceModel(
    val month: String = "",
    val expenseAmount: Long = 0L,
    val monthExpense: MonthExpense = MonthExpense(),
    val expenses: List<FinanceExpenses> = listOf(),
    val income: List<FinanceExpenses> = listOf(),
    val daySpent: Map<LocalDateTime, Long> = mapOf(),
)

data class FinanceExpenses(
    val category: CategoryEnum,
    val amount: Long,
    val count: Int,
    val percentage: Int,
)

data class MonthExpense(
    val incomeTotal: Long = 0L,
    val percentage: Long = 0L,
)
