package model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MonthDetailScreenModel(
    val monthAmount: Long,
    val daySpent: Map<LocalDateTime, Long> = mapOf(),
    val expenseScreenModel: List<ExpenseScreenModel>
)

@Serializable
data class ExpenseScreenModel(
    val id: Long,
    val amount: Long,
    val note: String,
    val category: String,
    val localDateTime: LocalDateTime,
    val date: String
)
