package presentation.screen.editexpense

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.carlosgub.myfinances.components.alertdialog.AlertDialog
import com.carlosgub.myfinances.components.toolbar.Toolbar
import com.carlosgub.myfinances.core.navigation.LocalNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import myfinances.shared.generated.resources.Res
import myfinances.shared.generated.resources.edit_expense_pop_up_message
import myfinances.shared.generated.resources.edit_expense_pop_up_title
import myfinances.shared.generated.resources.edit_expense_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import presentation.screen.editexpense.content.EditExpenseContent
import presentation.screen.editexpense.content.editExpenseObserver
import presentation.viewmodel.editexpense.EditExpenseViewModel

@Composable
fun EditExpenseScreen(
    id: Long,
    modifier: Modifier = Modifier,
    viewModel: EditExpenseViewModel = koinInject(),
) {
    val navigator = LocalNavController.current
    val scope = CoroutineScope(Dispatchers.Main)
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getExpense(
            id = id,
        )
    }
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            editExpenseObserver(
                sideEffect = sideEffect,
                navigator = navigator,
            )
        }
    }
    Scaffold(
        topBar = {
            EditExpenseToolbar(
                onBack = { navigator.popBackStack() },
                onDelete = {
                    viewModel.delete()
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        EditExpenseContent(
            paddingValues = paddingValues,
            state = state,
            intents = viewModel,
        )
    }
}

@Composable
private fun EditExpenseToolbar(
    onBack: () -> Unit,
    onDelete: () -> Unit,
) {
    var popUpVisible by remember { mutableStateOf(false) }
    Toolbar(
        hasNavigationIcon = true,
        title = stringResource(Res.string.edit_expense_title),
        navigation = onBack,
        leftIcon = Icons.Default.Delete,
        onLeftIconPressed = {
            popUpVisible = true
        },
    )
    if (popUpVisible) {
        DeletePopUp(
            onDelete = {
                onDelete()
                popUpVisible = false
            },
            onDismissRequest = {
                popUpVisible = false
            },
        )
    }
}

@Composable
private fun DeletePopUp(
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirmation = onDelete,
        dialogTitle = stringResource(Res.string.edit_expense_pop_up_title),
        dialogText = stringResource(Res.string.edit_expense_pop_up_message),
    )
}
