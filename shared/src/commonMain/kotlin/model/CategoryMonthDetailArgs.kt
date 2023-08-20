package model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryMonthDetailArgs(
    val category: String,
    val month: String,
)