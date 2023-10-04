package model

import kotlinx.serialization.Serializable

@Serializable
data class EditArgs(
    val financeEnum: FinanceEnum,
    val expenseScreenModel: ExpenseScreenModel
)
