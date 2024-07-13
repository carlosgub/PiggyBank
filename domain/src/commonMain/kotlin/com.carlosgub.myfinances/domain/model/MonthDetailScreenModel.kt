package com.carlosgub.myfinances.domain.model

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.datetime.LocalDateTime

data class MonthDetailScreenModel(
    val monthAmount: Long = 0L,
    val daySpent: ImmutableMap<LocalDateTime, Long> = persistentMapOf(),
    val expenseScreenModel: List<ExpenseScreenModel> = listOf(),
)

data class ExpenseScreenModel(
    val id: Long,
    val amount: Long,
    val note: String,
    val category: String,
    val localDateTime: LocalDateTime,
    val date: String,
)
