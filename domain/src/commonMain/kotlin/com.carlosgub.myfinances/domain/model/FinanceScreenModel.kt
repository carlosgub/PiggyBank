package com.carlosgub.myfinances.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

data class FinanceScreenModel(
    val month: Month = Month.JANUARY,
    val expenseAmount: Long = 0L,
    val monthExpense: MonthExpense = MonthExpense(),
    val expenses: ImmutableList<FinanceScreenExpenses> = persistentListOf(),
    val income: ImmutableList<FinanceScreenExpenses> = persistentListOf(),
    val daySpent: ImmutableMap<LocalDateTime, Long> = persistentMapOf(),
)

data class FinanceScreenExpenses(
    val category: com.carlosgub.myfinances.domain.model.CategoryEnum,
    val amount: Long,
    val count: Int,
    val percentage: Int,
)

data class MonthExpense(
    val incomeTotal: Double = 0.0,
    val percentage: Long = 0L,
)
