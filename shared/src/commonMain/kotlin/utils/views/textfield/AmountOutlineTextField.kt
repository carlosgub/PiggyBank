@file:OptIn(ExperimentalComposeUiApi::class)

package utils.views.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun AmountOutlineTextField(
    amountTextFieldValue: TextFieldValue,
    keyboard: SoftwareKeyboardController?,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit,
    showError: Boolean
) {
    OutlinedTextField(
        value = amountTextFieldValue,
        onValueChange = { value ->
            onValueChange(value.text)
        },
        label = {
            Text("Enter amount")
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboard?.hide()
                focusManager.clearFocus()
            }
        ),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
    )
    AnimatedVisibility(showError) {
        Text(
            text = "Enter an amount greather than zero",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}