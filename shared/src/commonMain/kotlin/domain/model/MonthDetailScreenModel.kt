package domain.model

import kotlinx.datetime.LocalDateTime

data class MonthDetailScreenModel(
    val monthAmount: Long = 0L,
    val daySpent: Map<LocalDateTime, Long> = mapOf(),
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
