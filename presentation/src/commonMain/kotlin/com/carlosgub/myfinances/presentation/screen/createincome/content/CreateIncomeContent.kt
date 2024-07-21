package com.carlosgub.myfinances.presentation.screen.createincome.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.carlosgub.myfinances.presentation.viewmodel.createincome.CreateIncomeScreenIntents
import com.carlosgub.myfinances.presentation.viewmodel.createincome.CreateIncomeScreenState
import com.carlosgub.myfinances.theme.spacing_4
import com.carlosgub.myfinances.theme.spacing_6
import myfinances.presentation.generated.resources.Res
import myfinances.presentation.generated.resources.create_income_button
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateIncomeContent(
    state: CreateIncomeScreenState,
    intents: CreateIncomeScreenIntents,
    modifier: Modifier = Modifier,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxSize().then(modifier)
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
            dayValueInMillis = { dateInMillis ->
                intents.showDateError(false)
                intents.setDate(dateInMillis)
            },
        )
        NoteOutlineTextField(
            keyboard = keyboard,
            focusManager = focusManager,
            onValueChange = { value ->
                intents.setNote(value)
                intents.showNoteError(false)
            },
            showError = state.showNoteError,
        )
        Box(
            modifier = Modifier.weight(1.0f),
        )
        CreateIncomeButton(
            intents = intents,
        )
    }
}

@Composable
private fun CreateIncomeButton(intents: CreateIncomeScreenIntents) {
    PrimaryButton(
        modifier = Modifier.padding(
            bottom = spacing_6,
        ),
        buttonText = stringResource(Res.string.create_income_button),
        onClick = {
            intents.create()
        },
    )
}
