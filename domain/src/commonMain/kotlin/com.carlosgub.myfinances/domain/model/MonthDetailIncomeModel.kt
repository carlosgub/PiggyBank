package com.carlosgub.myfinances.domain.model

import kotlinx.datetime.LocalDateTime

data class MonthDetailIncomeModel(
    val monthAmount: Long = 0L,
    val daySpent: Map<LocalDateTime, Long> = mapOf(),
    val incomeModelList: List<IncomeModel> = listOf(),
)
