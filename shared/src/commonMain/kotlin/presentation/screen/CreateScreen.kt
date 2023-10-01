@file:OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)

package presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import model.CategoryEnum
import model.CreateArgs
import model.CreateEnum
import model.FinanceEnum
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.viewmodel.CreateViewModel
import theme.ColorPrimary
import theme.Gray100
import theme.Gray400
import theme.Gray600
import theme.spacing_6
import utils.NoRippleInteractionSource
import utils.isExpense
import utils.views.Loading
import utils.views.PrimaryButton
import utils.views.Toolbar
import utils.views.textfield.AmountOutlineTextField
import utils.views.textfield.DayPicker
import utils.views.textfield.NoteOutlineTextField

@Composable
fun CreateScreen(
    viewModel: CreateViewModel = koinInject(),
    navigator: Navigator,
    args: CreateArgs
) {
    Scaffold(
        topBar = {
            CreateToolbar(
                createEnum = args.createEnum
            ) {
                navigator.goBack()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding()
                )
        ) {
            CreateContent(
                viewModel = viewModel,
                createEnum = args.createEnum
            )
            CreateObserver(
                viewModel = viewModel,
                navigator = navigator
            )
        }
    }
}

@Composable
private fun CreateContent(
    viewModel: CreateViewModel,
    createEnum: CreateEnum
) {
    val selectedSelected = viewModel.category.collectAsStateWithLifecycle().value
    val amountText = viewModel.amountField.collectAsStateWithLifecycle().value
    val showError = viewModel.showError.collectAsStateWithLifecycle().value
    val showNoteError = viewModel.showNoteError.collectAsStateWithLifecycle().value
    val showDateError = viewModel.showDateError.collectAsStateWithLifecycle().value
    val noteText = viewModel.noteField.collectAsStateWithLifecycle().value
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val amountTextFieldValue = remember {
        mutableStateOf(
            TextFieldValue(
                text = amountText,
                selection = TextRange(amountText.length)
            )
        )
    }
    amountTextFieldValue.value = TextFieldValue(
        text = amountText,
        selection = TextRange(amountText.length)
    )
    Column(
        modifier = Modifier.fillMaxSize()
            .clickable(
                interactionSource = NoRippleInteractionSource(),
                indication = null
            ) {
                keyboard?.hide()
                focusManager.clearFocus()
            }
            .padding(horizontal = 16.dp)
    ) {
        AmountOutlineTextField(
            amountTextFieldValue = amountTextFieldValue.value,
            keyboard = keyboard,
            focusManager = focusManager,
            onValueChange = { value ->
                viewModel.amountFieldChange(value)
                viewModel.showError(false)
            },
            showError = showError
        )
        if (createEnum.isExpense()) {
            CategoriesChips(
                selectedSelected,
                onChipPressed = { categoryEnumSelected ->
                    viewModel.setCategory(categoryEnumSelected)
                }
            )
        }
        DayPicker(
            showError = showDateError,
            dayValueInMillis = { dateInMillis ->
                viewModel.showDateError(false)
                viewModel.setDateInMillis(dateInMillis)
            }
        )
        NoteOutlineTextField(
            noteValue = noteText,
            keyboard = keyboard,
            focusManager = focusManager,
            onValueChange = { value ->
                viewModel.noteFieldChange(value)
                viewModel.showNoteError(false)
            },
            showNoteError
        )
        Box(
            modifier = Modifier.weight(1.0f)
        )
        val title = if (createEnum.isExpense()) "Expense" else "Income"
        PrimaryButton(
            modifier = Modifier.padding(
                bottom = spacing_6
            ),
            buttonText = "Create $title",
            onClick = {
                viewModel.create(createEnum)
            }
        )
    }
}

@Composable
private fun CreateObserver(
    viewModel: CreateViewModel,
    navigator: Navigator
) {
    when (viewModel.uiState.collectAsStateWithLifecycle().value.get()) {
        is GenericState.Error -> {
        }

        GenericState.Initial -> Unit
        GenericState.Loading -> Loading(Modifier.background(Gray400.copy(alpha = 0.5f)))
        is GenericState.Success -> {
            navigator.goBackWith(true)
        }

        else -> Unit
    }
}

@Composable
private fun CategoriesChips(
    selectedSelected: CategoryEnum,
    onChipPressed: (CategoryEnum) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val categoriesList = CategoryEnum.entries.filter { it.type == FinanceEnum.EXPENSE }
        Text(
            "Categories",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoriesList.forEach { categoryEnum ->
                CategoryChip(
                    categoryEnum,
                    selectedSelected,
                    onChipPressed = onChipPressed
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    categoryEnum: CategoryEnum,
    selected: CategoryEnum,
    onChipPressed: (CategoryEnum) -> Unit
) {
    val chipBackgroundColor = if (selected == categoryEnum) {
        ColorPrimary.copy(alpha = 0.2f)
    } else {
        Gray100
    }
    val contentColor = if (selected == categoryEnum) {
        ColorPrimary
    } else {
        Gray600
    }
    val chipBorderStroke = if (selected == categoryEnum) {
        BorderStroke(
            width = 1.dp,
            color = ColorPrimary
        )
    } else {
        BorderStroke(
            width = 1.dp,
            color = Gray400
        )
    }
    Chip(
        onClick = {
            onChipPressed(categoryEnum)
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = chipBackgroundColor
        ),
        shape = RoundedCornerShape(12.dp),
        border = chipBorderStroke
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = 6.dp,
                vertical = 4.dp
            )
        ) {
            Icon(
                imageVector = categoryEnum.icon,
                contentDescription = null,
                tint = contentColor
            )
            Text(
                categoryEnum.categoryName,
                color = contentColor,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun CreateToolbar(
    createEnum: CreateEnum,
    onBack: () -> Unit
) {
    val title = if (createEnum.isExpense()) "Expense" else "Income"
    Toolbar(
        hasNavigationIcon = true,
        title = "Create $title",
        navigation = onBack
    )
}
