package utils.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import theme.ColorPrimary
import theme.spacing_1
import theme.spacing_2
import theme.view_6

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    iconVector: ImageVector? = null,
    iconPainter: Painter? = null,
    buttonText: String,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    backgroundColor: Color = ColorPrimary,
    fontColor: Color = Color.White,
    shape: Shape = MaterialTheme.shapes.medium,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier =
            modifier
                .fillMaxWidth(),
        shape = shape,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = fontColor,
            ),
    ) {
        PrimaryButtonContent(
            iconVector = iconVector,
            iconPainter = iconPainter,
            buttonText = buttonText,
            fontColor = fontColor,
        )
    }
}

@Composable
fun PrimaryButtonContent(
    iconVector: ImageVector?,
    iconPainter: Painter?,
    buttonText: String,
    fontColor: Color = Color.White,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier =
            Modifier
                .padding(vertical = spacing_2)
                .fillMaxWidth(),
    ) {
        if (iconVector != null) {
            Icon(
                imageVector = iconVector,
                modifier =
                    Modifier
                        .padding(start = spacing_1)
                        .size(view_6),
                contentDescription = "button_icon",
                tint = fontColor,
            )
        }
        if (iconPainter != null) {
            Icon(
                painter = iconPainter,
                modifier =
                    Modifier
                        .padding(start = spacing_1)
                        .size(view_6),
                contentDescription = "button_icon",
                tint = fontColor,
            )
        }
        Text(
            text = buttonText.uppercase(),
            color = fontColor,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = spacing_2,
                        end = spacing_2,
                    ),
        )
    }
}
