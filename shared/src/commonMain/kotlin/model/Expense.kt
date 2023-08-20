package model

import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.ServerTimestampSerializer
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.TimestampSerializer

import kotlinx.serialization.Serializable

@Serializable
data class Expense(
    val amount: Int = 0,
    val userId: String = "",
    val note: String = "",
    val category: String = "",
    val month: String = "",
    @Serializable(with = TimestampSerializer::class)
    val timestamp: Timestamp = Timestamp.now(),
)