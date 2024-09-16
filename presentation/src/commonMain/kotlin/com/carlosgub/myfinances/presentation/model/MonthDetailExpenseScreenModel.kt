package com.carlosgub.myfinances.presentation.model

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.datetime.LocalDateTime

data class MonthDetailExpenseScreenModel(
    val monthAmount: Long = 0L,
    val daySpent: ImmutableMap<LocalDateTime, Long> = persistentMapOf(),
    val expenseScreenModelList: List<ExpenseScreenModel> = listOf(),
)