package model

import kotlinx.serialization.Serializable

@Serializable
data class Finance(
    val month: Map<String, Float>
)