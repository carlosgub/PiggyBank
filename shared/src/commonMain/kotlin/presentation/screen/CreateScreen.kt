package presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import core.sealed.GenericState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.CreateArgs
import model.FinanceEnum
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.viewmodel.create.CreateScreenIntents
import presentation.viewmodel.create.CreateScreenState
import presentation.viewmodel.create.CreateViewModel
import theme.spacing_4
import theme.spacing_6
import utils.NoRippleInteractionSource
import utils.isExpense
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
    val scope = CoroutineScope(Dispatchers.Main)
    val createScreenState by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            observeSideEffect(
                sideEffect = sideEffect,
                navigator = navigator
            )
        }
    }
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
                state = createScreenState,
                intents = viewModel,
                financeEnum = args.financeEnum
            )

        }
    }
}

@Composable
private fun CreateContent(
    state: CreateScreenState,
    intents: CreateScreenIntents,
    financeEnum: FinanceEnum
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
                state.category,
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
}

private fun observeSideEffect(
    sideEffect: GenericState<Unit>,
    navigator: Navigator
) {
    when (sideEffect) {
        is GenericState.Error -> {
        }

        is GenericState.Success -> {
            navigator.goBackWith(true)
        }
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
