package presentation.screen.create.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import model.FinanceEnum
import presentation.viewmodel.create.CreateScreenIntents
import presentation.viewmodel.create.CreateScreenState
import theme.spacing_4
import theme.spacing_6
import utils.NoRippleInteractionSource
import utils.isExpense
import utils.views.PrimaryButton
import utils.views.chips.CategoriesChips
import utils.views.textfield.AmountOutlineTextField
import utils.views.textfield.DayPicker
import utils.views.textfield.NoteOutlineTextField

@Composable
fun CreateContent(
    state: CreateScreenState,
    intents: CreateScreenIntents,
    financeEnum: FinanceEnum,
    modifier: Modifier
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxSize().then(modifier)
            .clickable(
                interactionSource = NoRippleInteractionSource(),
                indication = null
            ) {
                keyboard?.hide()
                focusManager.clearFocus()
            }
            .padding(horizontal = spacing_4)
    ) {
        AmountOutlineTextField(
            amountField = state.amountField,
            keyboard = keyboard,
            focusManager = focusManager,
            onValueChange = { value ->
                intents.setAmount(value)
                intents.showError(false)
            },
            showError = state.showError
        )
        if (financeEnum.isExpense()) {
            CategoriesChips(
                selectedSelected = state.category,
                onChipPressed = { categoryEnumSelected ->
                    intents.setCategory(categoryEnumSelected)
                }
            )
        }
        DayPicker(
            dateValue = state.date,
            showError = state.showDateError,
            dayValueInMillis = { dateInMillis ->
                intents.showDateError(false)
                intents.setDate(dateInMillis)
            }
        )
        NoteOutlineTextField(
            firstValue = "",
            keyboard = keyboard,
            focusManager = focusManager,
            onValueChange = { value ->
                intents.setNote(value)
                intents.showNoteError(false)
            },
            state.showNoteError
        )
        Box(
            modifier = Modifier.weight(1.0f)
        )
        CreateButton(
            financeEnum = financeEnum, intents = intents
        )
    }
}

@Composable
private fun CreateButton(
    financeEnum: FinanceEnum,
    intents: CreateScreenIntents
) {
    val title = if (financeEnum.isExpense()) "Expense" else "Income"
    PrimaryButton(
        modifier = Modifier.padding(
            bottom = spacing_6
        ),
        buttonText = "Create $title",
        onClick = {
            intents.create(financeEnum)
        }
    )
}