package model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MonthDetailScreenModel(
    val monthAmount: Long = 0L,
    val daySpent: Map<LocalDateTime, Long> = mapOf(),
    val expenseScreenModel: List<ExpenseScreenModel> = listOf()
)

@Serializable
data class ExpenseScreenModel(
    val id: Long,
    val amount: Long,
    val note: String,
    val category: String,
    val localDateTime: LocalDateTime,
    val date: String,
    val monthKey: String
)
