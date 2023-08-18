package model

import kotlinx.serialization.Serializable

@Serializable
data class FinanceScreenModel(
    val monthAmount: Int,
    val expenses: List<FinanceScreenExpenses>,
    val incomes: List<FinanceScreenExpenses>
)

@Serializable
data class FinanceScreenExpenses(
    val category: CategoryEnum,
    val amount: Int,
    val count: Int,
    val percentage: Int
)