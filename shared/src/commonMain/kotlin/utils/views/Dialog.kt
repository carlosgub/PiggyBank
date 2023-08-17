package utils.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Popup


@Composable
fun Dialog(
    barrierColor: Color = Color.Black.copy(alpha = 0.5f),
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    onClick = { onDismissRequest() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .background(barrierColor)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}