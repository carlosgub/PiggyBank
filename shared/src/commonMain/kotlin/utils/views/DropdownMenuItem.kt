package utils.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun DropdownMenuItem(
    icon: ImageVector,
    text: String,
    onItemClicked: () -> Unit
) {
    androidx.compose.material.DropdownMenuItem(
        onClick = onItemClicked,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.overline,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
