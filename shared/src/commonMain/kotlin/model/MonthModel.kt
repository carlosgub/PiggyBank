package model

import kotlinx.serialization.Serializable

@Serializable
data class MonthModel(
    val id: String,
    val month: String,
    val year: String
)
