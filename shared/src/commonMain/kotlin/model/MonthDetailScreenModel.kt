package model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MonthDetailScreenModel(
    val monthAmount: Int,
    val daySpent: Map<LocalDateTime, Int> = mapOf(),
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
