package domain.model

import kotlinx.datetime.LocalDateTime

data class FinanceModel(
    val id: Long,
    val amount: Long,
    val note: String,
    val category: String,
    val localDateTime: LocalDateTime,
    val date: String,
    val monthKey: String,
)
