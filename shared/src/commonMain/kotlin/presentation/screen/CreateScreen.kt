@file:OptIn(
    ExperimentalComposeUiApi::class
)

package presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import model.CreateArgs
import model.FinanceEnum
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.viewmodel.CreateViewModel
import theme.Gray400
import theme.spacing_6
import utils.NoRippleInteractionSource
import utils.isExpense
import utils.views.Loading
import utils.views.PrimaryButton
import utils.views.Toolbar
import utils.views.chips.CategoriesChips
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
                financeEnum = args.financeEnum
            ) {
                navigator.goBackWith(false)
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
                financeEnum = args.financeEnum
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
    financeEnum: FinanceEnum
) {
    val selectedSelected = viewModel.category.collectAsStateWithLifecycle().value
    val amountText = viewModel.amountField.collectAsStateWithLifecycle().value
    val showError = viewModel.showError.collectAsStateWithLifecycle().value
    val showNoteError = viewModel.showNoteError.collectAsStateWithLifecycle().value
    val showDateError = viewModel.showDateError.collectAsStateWithLifecycle().value
    val dateValue = viewModel.dateValue.collectAsStateWithLifecycle().value
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
        if (financeEnum.isExpense()) {
            CategoriesChips(
                selectedSelected,
                onChipPressed = { categoryEnumSelected ->
                    viewModel.setCategory(categoryEnumSelected)
                }
            )
        }
        DayPicker(
            dateValue = dateValue,
            showError = showDateError,
            dayValueInMillis = { dateInMillis ->
                viewModel.showDateError(false)
                viewModel.setDateInMillis(dateInMillis)
            }
        )
        NoteOutlineTextField(
            firstValue = "",
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
        val title = if (financeEnum.isExpense()) "Expense" else "Income"
        PrimaryButton(
            modifier = Modifier.padding(
                bottom = spacing_6
            ),
            buttonText = "Create $title",
            onClick = {
                viewModel.create(financeEnum)
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
private fun CreateToolbar(
    financeEnum: FinanceEnum,
    onBack: () -> Unit
) {
    val title = if (financeEnum.isExpense()) "Expense" else "Income"
    Toolbar(
        hasNavigationIcon = true,
        title = "Create $title",
        navigation = onBack
    )
}
