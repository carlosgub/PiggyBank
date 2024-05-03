package utils.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import theme.ColorSeparator
import theme.divider_thickness

@Composable
fun ExpenseDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = Modifier.fillMaxWidth().then(modifier),
        thickness = divider_thickness,
        color = ColorSeparator,
    )
}
