package com.carlosgub.myfinances.domain.model

import kotlinx.datetime.LocalDateTime

data class MonthDetailExpenseModel(
    val monthAmount: Long = 0L,
    val daySpent: Map<LocalDateTime, Long> = mapOf(),
    val expenseModelList: List<ExpenseModel> = listOf(),
)
