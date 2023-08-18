package model

import kotlinx.serialization.Serializable

@Serializable
data class Finance(
    val amount: Int = 0,
    val category: Map<String, FinanceMonthCategoryDetail> = mapOf()
)

@Serializable
data class FinanceMonthCategoryDetail(
    val amount: Int = 0,
    val count: Int = 0
)