@file:OptIn(ExperimentalResourceApi::class)

package presentation.screen.editexpense.content

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
import myapplication.shared.generated.resources.Res
import myapplication.shared.generated.resources.edit_expense_button
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import presentation.viewmodel.editexpense.EditExpenseScreenIntents
import presentation.viewmodel.editexpense.EditExpenseScreenState
import theme.spacing_4
import theme.spacing_6
import utils.NoRippleInteractionSource
import utils.views.PrimaryButton
import utils.views.chips.CategoriesChips
import utils.views.textfield.AmountOutlineTextField
import utils.views.textfield.DayPicker
import utils.views.textfield.NoteOutlineTextField

@Composable
fun EditExpenseContent(
    paddingValues: PaddingValues,
    state: EditExpenseScreenState,
    intents: EditExpenseScreenIntents,
) {
    if (state.initialDataLoaded) {
        val keyboard = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        Column(
            modifier =
                Modifier
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
            CategoriesChips(
                selectedSelected = state.category,
                onChipPressed = { categoryEnumSelected ->
                    intents.setCategory(categoryEnumSelected)
                },
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
            )
            Box(
                modifier = Modifier.weight(1.0f),
            )
            EditExpenseButton(
                intents = intents,
            )
        }
    }
}

@Composable
private fun EditExpenseButton(intents: EditExpenseScreenIntents) {
    PrimaryButton(
        modifier =
            Modifier.padding(
                bottom = spacing_6,
            ),
        buttonText = stringResource(Res.string.edit_expense_button),
        onClick = {
            intents.edit()
        },
    )
}
