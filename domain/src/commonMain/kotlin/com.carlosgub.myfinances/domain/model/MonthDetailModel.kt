package com.carlosgub.myfinances.domain.model

import kotlinx.datetime.LocalDateTime

data class MonthDetailModel(
    val monthAmount: Long = 0L,
    val daySpent: Map<LocalDateTime, Long> = mapOf(),
    val expenseModel: List<ExpenseModel> = listOf(),
)
