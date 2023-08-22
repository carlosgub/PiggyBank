package model

import kotlinx.serialization.Serializable

@Serializable
data class MonthDetailScreenModel(
    val monthAmount: Int,
    val daySpent: Map<String, Int> = mapOf(),
    val expenseScreenModel: List<ExpenseScreenModel>,
)

@Serializable
data class ExpenseScreenModel(
    val amount: Int,
    val userId: String,
    val note: String,
    val category: String,
    val month: String,
    val day: String,
)