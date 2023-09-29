package data.model

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
    val dateInMillis:Long = 0L,
    @Serializable(with = TimestampSerializer::class)
    val timestamp: Timestamp = Timestamp.now()
)
