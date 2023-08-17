@file:OptIn(
    ExperimentalLayoutApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)

package presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import model.CategoryEnum
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.viewmodel.CreateExpenseViewModel
import theme.ColorPrimary
import theme.Gray100
import theme.Gray400
import theme.Gray600
import theme.spacing_6
import utils.views.PrimaryButton
import utils.views.Toolbar

@Composable
fun CreateExpenseScreen(
    viewModel: CreateExpenseViewModel = koinInject(),
    navigator: Navigator
) {

    val selectedSelected = viewModel.category.collectAsStateWithLifecycle().value
    val amountText = viewModel.amountField.collectAsStateWithLifecycle().value
    val showError = viewModel.showError.collectAsStateWithLifecycle().value
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
    Scaffold(
        topBar = {
            CreateExpenseToolbar(
                onBack = {
                    navigator.goBack()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
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
            NoteOutlineTextField(
                noteValue = noteText,
                keyboard = keyboard,
                focusManager = focusManager,
                onValueChange = { value ->
                    viewModel.noteFieldChange(value)
                }
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
}

@Composable
private fun AmountOutlineTextField(
    amountTextFieldValue: TextFieldValue,
    keyboard: SoftwareKeyboardController?,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit,
    showError: Boolean
) {
    OutlinedTextField(
        value = amountTextFieldValue,
        onValueChange = { value ->
            onValueChange(value.text)
        },
        label = {
            Text("Enter amount")
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboard?.hide()
                focusManager.clearFocus()
            }
        ),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
    )
    AnimatedVisibility(showError) {
        Text(
            text = "Enter an amount greather than zero",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun NoteOutlineTextField(
    noteValue: String,
    keyboard: SoftwareKeyboardController?,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = noteValue,
        onValueChange = { value ->
            onValueChange(value)
        },
        label = {
            Text("Note")
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboard?.hide()
                focusManager.clearFocus()
            }
        ),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun CategoriesChips(
    selectedSelected: CategoryEnum,
    onChipPressed: (CategoryEnum) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val categoriesList = CategoryEnum.entries.toList()
        Text(
            "Categories",
            style = MaterialTheme.typography.body1,
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
                style = MaterialTheme.typography.caption,
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
        title = "Create Expense",
        navigationIcon = Icons.Default.ArrowBack,
        navigation = onBack,
    )
}