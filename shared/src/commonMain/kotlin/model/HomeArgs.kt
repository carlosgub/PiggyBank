package model

import kotlinx.serialization.Serializable

@Serializable
data class HomeArgs(
    val monthKey: String,
    val isHome: Boolean = false
)
