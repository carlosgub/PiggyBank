package com.carlosgub.myfinances.domain.model

import kotlinx.datetime.LocalDateTime

data class ExpenseModel(
    val id: Long,
    val amount: Long,
    val note: String,
    val category: String,
    val localDateTime: LocalDateTime,
    val date: String,
    val monthKey: String,
)
