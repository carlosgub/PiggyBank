package com.carlosgub.myfinances.presentation.model

import com.carlosgub.myfinances.domain.model.FinanceExpenses
import com.carlosgub.myfinances.domain.model.MonthExpense
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.datetime.LocalDateTime

data class FinanceScreenModel(
    val month: String = "",
    val expenseAmount: Long = 0L,
    val monthExpense: MonthExpense = MonthExpense(),
    val expenses: ImmutableList<FinanceExpenses> = persistentListOf(),
    val income: ImmutableList<FinanceExpenses> = persistentListOf(),
    val daySpent: ImmutableMap<LocalDateTime, Long> = persistentMapOf(),
)
