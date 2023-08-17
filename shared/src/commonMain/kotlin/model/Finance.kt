package model

import kotlinx.serialization.Serializable

@Serializable
data class Finance(
    val month: Map<String, FinanceMonthDetail> = mapOf()
)

@Serializable
data class FinanceMonthDetail(
    val amount: Int = 0,
    val category: Map<String, FinanceMonthCategoryDetail>
)

@Serializable
data class FinanceMonthCategoryDetail(
    val amount: Int = 0,
    val count: Int = 0
)