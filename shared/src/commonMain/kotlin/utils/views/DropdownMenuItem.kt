package utils.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import theme.spacing_2

@Composable
fun DropdownMenuItem(
    icon: ImageVector,
    text: String,
    onItemClicked: () -> Unit
) {
    androidx.compose.material3.DropdownMenuItem(
        onClick = onItemClicked,
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = spacing_2),
                color = Color.Black
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black
            )
        }
    )
}
