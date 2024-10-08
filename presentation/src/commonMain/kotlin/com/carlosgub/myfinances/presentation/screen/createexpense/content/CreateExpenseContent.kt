@file:OptIn(ExperimentalLayoutApi::class)

package com.carlosgub.myfinances.presentation.screen.createexpense.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.carlosgub.myfinances.components.button.PrimaryButton
import com.carlosgub.myfinances.components.chips.CategoryChip
import com.carlosgub.myfinances.components.textfield.AmountOutlineTextField
import com.carlosgub.myfinances.components.textfield.DayPicker
import com.carlosgub.myfinances.components.textfield.NoteOutlineTextField
import com.carlosgub.myfinances.core.utils.NoRippleInteractionSource
import com.carlosgub.myfinances.domain.model.CategoryEnum
import com.carlosgub.myfinances.domain.model.FinanceEnum
import com.carlosgub.myfinances.presentation.viewmodel.createexpense.CreateExpenseScreenIntents
import com.carlosgub.myfinances.presentation.viewmodel.createexpense.CreateExpenseScreenState
import com.carlosgub.myfinances.theme.spacing_2
import com.carlosgub.myfinances.theme.spacing_4
import com.carlosgub.myfinances.theme.spacing_6
import org.jetbrains.compose.resources.stringResource
import piggybank.presentation.generated.resources.Res
import piggybank.presentation.generated.resources.categories_header
import piggybank.presentation.generated.resources.create_expense_button
import piggybank.presentation.generated.resources.create_expense_error_text

@Composable
fun CreateExpenseContent(
    state: CreateExpenseScreenState,
    intents: CreateExpenseScreenIntents,
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
        CategoriesChips(
            categoryEnumSelected = state.category,
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
            keyboard = keyboard,
            focusManager = focusManager,
            onValueChange = { value ->
                intents.setNote(value)
                intents.showNoteError(false)
            },
            showError = state.showNoteError,
            errorText = stringResource(Res.string.create_expense_error_text),
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
        modifier = Modifier.padding(
            bottom = spacing_6,
        ),
        buttonText = stringResource(Res.string.create_expense_button),
        onClick = {
            intents.create()
        },
    )
}

@Composable
private fun CategoriesChips(
    categoryEnumSelected: CategoryEnum,
    onChipPressed: (CategoryEnum) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        val categoriesList = CategoryEnum.entries.filter { it.type == FinanceEnum.EXPENSE }
        Text(
            text = stringResource(Res.string.categories_header),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = spacing_4),
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing_2),
        ) {
            categoriesList.forEach { categoryEnum ->
                CategoryChip(
                    text = categoryEnum.categoryName,
                    icon = categoryEnum.icon,
                    selected = categoryEnumSelected.categoryName == categoryEnum.categoryName,
                    onChipPressed = { categoryName ->
                        onChipPressed(CategoryEnum.getCategoryEnumFromCategoryName(categoryName))
                    },
                )
            }
        }
    }
}
