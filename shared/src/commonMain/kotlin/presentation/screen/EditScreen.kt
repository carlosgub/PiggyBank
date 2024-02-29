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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import model.EditArgs
import model.FinanceEnum
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.viewmodel.EditScreenIntents
import presentation.viewmodel.EditScreenState
import presentation.viewmodel.EditViewModel
import presentation.viewmodel.state.EditSideEffects
import theme.Gray400
import theme.spacing_4
import theme.spacing_6
import utils.NoRippleInteractionSource
import utils.getCategoryEnumFromName
import utils.isExpense
import utils.toMillis
import utils.views.AlertDialogFinance
import utils.views.Loading
import utils.views.PrimaryButton
import utils.views.Toolbar
import utils.views.chips.CategoriesChips
import utils.views.textfield.AmountOutlineTextField
import utils.views.textfield.DayPicker
import utils.views.textfield.NoteOutlineTextField

@Composable
fun EditScreen(
    viewModel: EditViewModel = koinInject(),
    navigator: Navigator,
    args: EditArgs
) {
    val editScreenState by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val editScreenSideEffect by viewModel.container.sideEffectFlow.collectAsState(
        EditSideEffects.Initial
    )
    LaunchedEffect(Unit) {
        viewModel.updateValues(args.expenseScreenModel)
    }
    Scaffold(
        topBar = {
            EditToolbar(
                financeEnum = args.financeEnum,
                onBack = { navigator.goBackWith(false) },
                onDelete = {
                    viewModel.delete(
                        id = args.expenseScreenModel.id,
                        financeEnum = args.financeEnum,
                        monthKey = args.expenseScreenModel.monthKey
                    )
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
            EditContent(
                state = editScreenState,
                intents = viewModel,
                financeEnum = args.financeEnum,
                id = args.expenseScreenModel.id
            )
            EditObserver(
                sideEffect = editScreenSideEffect,
                navigator = navigator
            )
        }
    }
}

@Composable
private fun EditContent(
    state: EditScreenState,
    intents: EditScreenIntents,
    financeEnum: FinanceEnum,
    id: Long
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val amountTextFieldValue = remember {
        mutableStateOf(
            TextFieldValue(
                text = state.amountField,
                selection = TextRange(state.amountField.length)
            )
        )
    }
    amountTextFieldValue.value = TextFieldValue(
        text = state.amountField,
        selection = TextRange(state.amountField.length)
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
            .padding(horizontal = spacing_4)
    ) {
        AmountOutlineTextField(
            amountTextFieldValue = amountTextFieldValue.value,
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
            dateInMillis = state.dateInMillis,
            dayValueInMillis = { dateInMillis ->
                intents.showDateError(false)
                intents.setDate(dateInMillis)
            }
        )
        NoteOutlineTextField(
            firstValue = state.note,
            keyboard = keyboard,
            focusManager = focusManager,
            onValueChange = { value ->
                intents.setNote(value)
                intents.showNoteError(false)
            },
            showError = state.showNoteError
        )
        Box(
            modifier = Modifier.weight(1.0f)
        )
        val title = if (financeEnum.isExpense()) "Expense" else "Income"
        PrimaryButton(
            modifier = Modifier.padding(
                bottom = spacing_6
            ),
            buttonText = "Edit $title",
            onClick = {
                intents.create(
                    financeEnum = financeEnum,
                    id = id
                )
            }
        )
    }
}

@Composable
private fun EditObserver(
    sideEffect: EditSideEffects,
    navigator: Navigator
) {
    when (sideEffect) {
        is EditSideEffects.Error -> {
        }

        EditSideEffects.Initial -> Unit
        EditSideEffects.Loading -> Loading(Modifier.background(Gray400.copy(alpha = 0.5f)))
        is EditSideEffects.Success -> navigator.goBackWith(true)
        EditSideEffects.Delete -> navigator.goBackWith(true)
    }
}

@Composable
private fun EditToolbar(
    financeEnum: FinanceEnum,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    val title = if (financeEnum.isExpense()) "Expense" else "Income"
    val popUpVisible = remember { mutableStateOf(false) }
    Toolbar(
        hasNavigationIcon = true,
        title = "Edit $title",
        navigation = onBack,
        leftIcon = Icons.Default.Delete,
        onLeftIconPressed = {
            popUpVisible.value = true
        }
    )
    if (popUpVisible.value) {
        DeletePopUp(
            popUpVisible = popUpVisible,
            onDelete = onDelete,
            type = financeEnum.name
        )
    }
}

@Composable
private fun DeletePopUp(
    popUpVisible: MutableState<Boolean>,
    onDelete: () -> Unit,
    type: String
) {
    AlertDialogFinance(
        onDismissRequest = {
            popUpVisible.value = false
        },
        onConfirmation = {
            onDelete()
            popUpVisible.value = false
        },
        dialogTitle = "Delete",
        dialogText = "Do you want to delete this ${type.lowercase()}?"
    )
}
