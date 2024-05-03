@file:OptIn(ExperimentalResourceApi::class)

package presentation.screen.createincome.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import myapplication.shared.generated.resources.Res
import myapplication.shared.generated.resources.create_income_button
import myapplication.shared.generated.resources.create_income_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import presentation.viewmodel.createincome.CreateIncomeScreenIntents
import presentation.viewmodel.createincome.CreateIncomeScreenState
import theme.spacing_4
import theme.spacing_6
import utils.NoRippleInteractionSource
import utils.views.PrimaryButton
import utils.views.textfield.AmountOutlineTextField
import utils.views.textfield.DayPicker
import utils.views.textfield.NoteOutlineTextField

@Composable
fun CreateIncomeContent(
    state: CreateIncomeScreenState,
    intents: CreateIncomeScreenIntents,
    modifier: Modifier,
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
