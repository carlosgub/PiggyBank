package utils.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.carlosgub.myfinances.theme.ColorSeparator
import com.carlosgub.myfinances.theme.divider_thickness

@Composable
fun ExpenseDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth().then(modifier),
        thickness = divider_thickness,
        color = ColorSeparator,
    )
}
