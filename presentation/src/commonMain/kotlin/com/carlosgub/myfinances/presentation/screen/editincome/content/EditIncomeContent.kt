package com.carlosgub.myfinances.presentation.screen.editincome.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.carlosgub.myfinances.components.button.PrimaryButton
import com.carlosgub.myfinances.components.textfield.AmountOutlineTextField
import com.carlosgub.myfinances.components.textfield.DayPicker
import com.carlosgub.myfinances.components.textfield.NoteOutlineTextField
import com.carlosgub.myfinances.core.utils.NoRippleInteractionSource
import com.carlosgub.myfinances.presentation.viewmodel.editincome.EditIncomeScreenIntents
import com.carlosgub.myfinances.presentation.viewmodel.editincome.EditIncomeScreenState
import com.carlosgub.myfinances.theme.spacing_4
import com.carlosgub.myfinances.theme.spacing_6
import myfinances.presentation.generated.resources.Res
import myfinances.presentation.generated.resources.edit_income_button
import myfinances.presentation.generated.resources.edit_income_error_text
import org.jetbrains.compose.resources.stringResource

@Composable
fun EditIncomeContent(
    paddingValues: PaddingValues,
    state: EditIncomeScreenState,
    intents: EditIncomeScreenIntents,
    modifier: Modifier = Modifier,
) {
    if (state.initialDataLoaded) {
        val keyboard = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .clickable(
                    interactionSource = NoRippleInteractionSource(),
                    indication = null,
                ) {
                    keyboard?.hide()
                    focusManager.clearFocus()
                }
                .padding(horizontal = spacing_4),
        ) {
            AmountOutlineTextField(
                amountField = state.amountField,
                keyboard = keyboard,
                focusManager = focusManager,
                onValueChange = { value ->
                    intents.setAmount(value)
                    intents.showError(false)
                },
                showError = state.showError,
            )
            DayPicker(
                dateValue = state.date,
                showError = state.showDateError,
                dateInMillis = state.dateInMillis,
                dayValueInMillis = { dateInMillis ->
                    intents.showDateError(false)
                    intents.setDate(dateInMillis)
                },
            )
            NoteOutlineTextField(
                firstValue = state.note,
                keyboard = keyboard,
                focusManager = focusManager,
                onValueChange = { value ->
                    intents.setNote(value)
                    intents.showNoteError(false)
                },
                showError = state.showNoteError,
                errorText = stringResource(Res.string.edit_income_error_text),
            )
            Box(
                modifier = Modifier.weight(1.0f),
            )
            EditButton(
                intents = intents,
            )
        }
    }
}

@Composable
private fun EditButton(intents: EditIncomeScreenIntents) {
    PrimaryButton(
        modifier = Modifier.padding(
            bottom = spacing_6,
        ),
        buttonText = stringResource(Res.string.edit_income_button),
        onClick = {
            intents.edit()
        },
    )
}
