package data.model

import kotlinx.serialization.Serializable

@Serializable
data class Month(
    val months: Map<String, String?> = mapOf()
)
