@file:OptIn(FlowPreview::class)

package com.carlosgub.myfinances.components.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.carlosgub.myfinances.theme.spacing_2
import com.carlosgub.myfinances.theme.spacing_4
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import myfinances.components.generated.resources.Res
import myfinances.components.generated.resources.finance_note_outline_text_field_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoteOutlineTextField(
    keyboard: SoftwareKeyboardController?,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit,
    showError: Boolean,
    errorText: String,
    modifier: Modifier = Modifier,
    firstValue: String = "",
) {
    var text by remember { mutableStateOf("") }
    val latestOnClick by rememberUpdatedState(onValueChange)
    LaunchedEffect(Unit) {
        text = firstValue
        snapshotFlow { text }
            .debounce(500L)
            .distinctUntilChanged()
            .onEach { value ->
                latestOnClick(value)
            }
            .launchIn(this)
    }
    OutlinedTextField(
        value = text,
        onValueChange = { value ->
            text = value
        },
        label = {
            Text(stringResource(Res.string.finance_note_outline_text_field_label))
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
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
            text = errorText,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = spacing_4),
        )
    }
}
