package presentation.screen.edit

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
import model.FinanceEnum
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.koin.compose.koinInject
import presentation.screen.edit.content.EditContent
import presentation.screen.edit.content.editObserver
import presentation.viewmodel.edit.EditViewModel
import utils.getFinanceEnumFromName
import utils.isExpense
import utils.views.AlertDialogFinance
import utils.views.Toolbar

@Composable
fun EditScreen(
    viewModel: EditViewModel = koinInject(),
    id: Long,
    financeName: String
) {
    val navigator = LocalNavController.current
    val scope = CoroutineScope(Dispatchers.Main)
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val financeEnum = getFinanceEnumFromName(financeName)
    LaunchedEffect(Unit) {
        viewModel.getFinance(
            id = id,
            financeEnum = financeEnum
        )
    }
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            editObserver(
                sideEffect = sideEffect,
                navigator = navigator
            )
        }
    }
    Scaffold(
        topBar = {
            EditToolbar(
                financeEnum = financeEnum,
                onBack = { navigator.popBackStack() },
                onDelete = {
                    viewModel.delete()
                }
            )
        }
    ) { paddingValues ->
        EditContent(
            paddingValues = paddingValues,
            state = state,
            intents = viewModel
        )
    }
}

@Composable
private fun EditToolbar(
    financeEnum: FinanceEnum,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    val title = if (financeEnum.isExpense()) "Expense" else "Income"
    var popUpVisible by remember { mutableStateOf(false) }
    Toolbar(
        hasNavigationIcon = true,
        title = "Edit $title",
        navigation = onBack,
        leftIcon = Icons.Default.Delete,
        onLeftIconPressed = {
            popUpVisible = true
        }
    )
    if (popUpVisible) {
        DeletePopUp(
            type = financeEnum.name,
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
    type: String,
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialogFinance(
        onDismissRequest = onDismissRequest,
        onConfirmation = onDelete,
        dialogTitle = "Delete",
        dialogText = "Do you want to delete this ${type.lowercase()}?"
    )
}
