package model

import kotlinx.serialization.Serializable

@Serializable
data class CreateArgs(
    val financeEnum: FinanceEnum
)