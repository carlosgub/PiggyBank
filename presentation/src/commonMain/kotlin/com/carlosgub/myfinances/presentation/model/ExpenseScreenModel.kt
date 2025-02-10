package com.carlosgub.myfinances.presentation.model

import kotlinx.datetime.LocalDateTime

data class ExpenseScreenModel(
    val id: Long,
    val amount: Long,
    val note: String,
    val category: String,
    val localDateTime: LocalDateTime,
    val date: String,
)
