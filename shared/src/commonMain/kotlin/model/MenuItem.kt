package model

import androidx.compose.ui.graphics.vector.ImageVector
import dev.gitlive.firebase.firestore.ServerTimestampSerializer
import dev.gitlive.firebase.firestore.Timestamp

import kotlinx.serialization.Serializable

data class MenuItem(
    val name: String,
    val icon: ImageVector,
    val onItemClicked: () -> Unit
)