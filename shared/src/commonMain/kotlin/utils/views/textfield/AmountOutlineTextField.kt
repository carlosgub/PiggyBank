package utils.views.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import theme.spacing_2
import theme.spacing_4

@Composable
fun AmountOutlineTextField(
    amountField: String,
    keyboard: SoftwareKeyboardController?,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit,
    showError: Boolean,
    modifier: Modifier = Modifier,
) {
    var amountTextFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = amountField,
                selection = TextRange(amountField.length),
            ),
        )
    }
    amountTextFieldValue =
        TextFieldValue(
            text = amountField,
            selection = TextRange(amountField.length),
        )
    OutlinedTextField(
        value = amountTextFieldValue,
        onValueChange = { value ->
            onValueChange(value.text)
        },
        label = {
            Text("Enter amount")
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboard?.hide()
                focusManager.clearFocus()
            },
        ),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(top = spacing_2)
            .fillMaxWidth(),
    )
    AnimatedVisibility(showError) {
        Text(
            text = "Enter an amount greather than zero",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = spacing_4),
        )
    }
}
