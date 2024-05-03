package presentation.screen.createexpense.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import presentation.viewmodel.createexpense.CreateExpenseScreenIntents
import presentation.viewmodel.createexpense.CreateExpenseScreenState
import theme.spacing_4
import theme.spacing_6
import utils.NoRippleInteractionSource
import utils.views.PrimaryButton
import utils.views.chips.CategoriesChips
import utils.views.textfield.AmountOutlineTextField
import utils.views.textfield.DayPicker
import utils.views.textfield.NoteOutlineTextField

@Composable
fun CreateExpenseContent(
    state: CreateExpenseScreenState,
    intents: CreateExpenseScreenIntents,
    modifier: Modifier,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier =
            Modifier.fillMaxSize().then(modifier)
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
            dayValueInMillis = { dateInMillis ->
                intents.showDateError(false)
                intents.setDate(dateInMillis)
            },
        )
        NoteOutlineTextField(
            firstValue = "",
            keyboard = keyboard,
            focusManager = focusManager,
            onValueChange = { value ->
                intents.setNote(value)
                intents.showNoteError(false)
            },
            state.showNoteError,
        )
        Box(
            modifier = Modifier.weight(1.0f),
        )
        CreateExpenseButton(
            intents = intents,
        )
    }
}

@Composable
private fun CreateExpenseButton(intents: CreateExpenseScreenIntents) {
    PrimaryButton(
        modifier =
            Modifier.padding(
                bottom = spacing_6,
            ),
        buttonText = "Create Expense",
        onClick = {
            intents.create()
        },
    )
}
