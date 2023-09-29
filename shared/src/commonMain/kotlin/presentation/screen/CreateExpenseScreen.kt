@file:OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)

package presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.CategoryEnum
import model.FinanceEnum
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.viewmodel.CreateExpenseViewModel
import theme.ColorPrimary
import theme.Gray100
import theme.Gray400
import theme.Gray600
import theme.spacing_6
import utils.NoRippleInteractionSource
import utils.numberToTwoDigits
import utils.views.PrimaryButton
import utils.views.Toolbar
import utils.views.textfield.AmountOutlineTextField
import utils.views.textfield.NoteOutlineTextField

@Composable
fun CreateExpenseScreen(
    viewModel: CreateExpenseViewModel = koinInject(),
    navigator: Navigator
) {
    Scaffold(
        topBar = {
            CreateExpenseToolbar(
                onBack = {
                    navigator.goBack()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding()
                )
        ) {
            CreateExpenseContent(viewModel)
            CreateExpenseObserver(viewModel, navigator)
        }
    }
}

@Composable
private fun CreateExpenseContent(viewModel: CreateExpenseViewModel) {
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
        CategoriesChips(
            selectedSelected,
            onChipPressed = { categoryEnumSelected ->
                viewModel.setCategory(categoryEnumSelected)
            }
        )
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
        PrimaryButton(
            modifier = Modifier.padding(
                bottom = spacing_6
            ),
            buttonText = "Create Expense",
            onClick = {
                viewModel.createExpense()
            }
        )
    }
}

@Composable
private fun DayPicker(
    showError: Boolean,
    dayValueInMillis: (Long) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var dayValue by remember { mutableStateOf("") }
    OutlinedTextField(
        value = dayValue,
        onValueChange = {},
        label = {
            Text(
                text = "Enter day",
                color = Color.Black
            )
        },
        keyboardActions = KeyboardActions(),
        shape = MaterialTheme.shapes.small,
        readOnly = true,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .clickable {
                isVisible = true
            },
        enabled = false,
        leadingIcon = {
            Icon(
                Icons.Default.CalendarMonth,
                contentDescription = null
            )
        }
    )
    AnimatedVisibility(showError) {
        Text(
            text = "Enter a date",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
    AnimatedVisibility(isVisible) {
        DatePickerDialog(
            onDismissRequest = {
                isVisible = false
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier,
            tonalElevation = 8.dp,
            confirmButton = {
                Button(
                    onClick = {
                        isVisible = false
                    },

                    ) {
                    Text("Ok")
                }
            },
            content = {
                DatePicker(
                    state = datePickerState
                )
            }
        )
    }
    datePickerState.selectedDateMillis?.let {
        dayValueInMillis(it)
        val date = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
        dayValue =
            "${date.dayOfMonth.numberToTwoDigits()}/" +
                    "${date.monthNumber.numberToTwoDigits()}/" +
                    "${date.year}"
    }
}

@Composable
private fun CreateExpenseObserver(
    viewModel: CreateExpenseViewModel,
    navigator: Navigator
) {
    when (viewModel.uiState.collectAsStateWithLifecycle().value.get()) {
        is GenericState.Error -> {
        }

        GenericState.Initial -> Unit
        GenericState.Loading -> Unit
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
private fun CreateExpenseToolbar(
    onBack: () -> Unit
) {
    Toolbar(
        hasNavigationIcon = true,
        title = "Create Expense",
        navigation = onBack
    )
}
