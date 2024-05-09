package utils.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import theme.spacing_1
import theme.spacing_2
import theme.spacing_4

@Composable
fun <E> DataZero(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    hasButton: Boolean = false,
    valueToPass: E? = null,
    onButtonClick: (E) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier =
            Modifier.padding(
                top = spacing_1,
                start = spacing_2,
                end = spacing_2
            )
        )
        Text(
            message,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier =
            Modifier.padding(
                top = spacing_1,
                start = spacing_2,
                end = spacing_2
            )
        )
        if (hasButton) {
            PrimaryButton(
                buttonText = "Add new",
                iconVector = Icons.Default.Add,
                shape = MaterialTheme.shapes.extraLarge,
                onClick = {
                    valueToPass?.let(onButtonClick)
                },
                modifier =
                Modifier
                    .padding(
                        top = spacing_4,
                        start = spacing_2,
                        end = spacing_2
                    )
                    .width(IntrinsicSize.Max)
            )
        }
    }
}
