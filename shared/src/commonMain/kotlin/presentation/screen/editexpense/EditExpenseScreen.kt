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
import core.navigation.LocalNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.koin.compose.koinInject
import presentation.screen.editexpense.content.EditExpenseContent
import presentation.screen.editexpense.content.editExpenseObserver
import presentation.viewmodel.edit.EditExpenseViewModel
import utils.views.AlertDialogFinance
import utils.views.Toolbar

@Composable
fun EditExpenseScreen(
    viewModel: EditExpenseViewModel = koinInject(),
    id: Long
) {
    val navigator = LocalNavController.current
    val scope = CoroutineScope(Dispatchers.Main)
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getExpense(
            id = id
        )
    }
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            editExpenseObserver(
                sideEffect = sideEffect,
                navigator = navigator
            )
        }
    }
    Scaffold(
        topBar = {
            EditExpenseToolbar(
                onBack = { navigator.popBackStack() },
                onDelete = {
                    viewModel.delete()
                }
            )
        }
    ) { paddingValues ->
        EditExpenseContent(
            paddingValues = paddingValues,
            state = state,
            intents = viewModel
        )
    }
}

@Composable
private fun EditExpenseToolbar(
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    var popUpVisible by remember { mutableStateOf(false) }
    Toolbar(
        hasNavigationIcon = true,
        title = "Edit Expense",
        navigation = onBack,
        leftIcon = Icons.Default.Delete,
        onLeftIconPressed = {
            popUpVisible = true
        }
    )
    if (popUpVisible) {
        DeletePopUp(
            onDelete = {
                onDelete()
                popUpVisible = false
            },
            onDismissRequest = {
                popUpVisible = false
            }
        )
    }
}

@Composable
private fun DeletePopUp(
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialogFinance(
        onDismissRequest = onDismissRequest,
        onConfirmation = onDelete,
        dialogTitle = "Delete",
        dialogText = "Do you want to delete this expense?"
    )
}
