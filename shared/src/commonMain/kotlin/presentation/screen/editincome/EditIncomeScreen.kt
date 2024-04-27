package presentation.screen.editincome

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
import presentation.screen.editincome.content.EditIncomeContent
import presentation.screen.editincome.content.editIncomeObserver
import presentation.viewmodel.editincome.EditIncomeViewModel
import utils.views.AlertDialogFinance
import utils.views.Toolbar

@Composable
fun EditIncomeScreen(
    viewModel: EditIncomeViewModel = koinInject(),
    id: Long,
) {
    val navigator = LocalNavController.current
    val scope = CoroutineScope(Dispatchers.Main)
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getIncome(
            id = id
        )
    }
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            editIncomeObserver(
                sideEffect = sideEffect,
                navigator = navigator
            )
        }
    }
    Scaffold(
        topBar = {
            EditIncomeToolbar(
                onBack = { navigator.popBackStack() },
                onDelete = {
                    viewModel.delete()
                }
            )
        }
    ) { paddingValues ->
        EditIncomeContent(
            paddingValues = paddingValues,
            state = state,
            intents = viewModel
        )
    }
}

@Composable
private fun EditIncomeToolbar(
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    var popUpVisible by remember { mutableStateOf(false) }
    Toolbar(
        hasNavigationIcon = true,
        title = "Edit Income",
        navigation = onBack,
        leftIcon = Icons.Default.Delete,
        onLeftIconPressed = {
            popUpVisible = true
        }
    )
    if (popUpVisible) {
        DeleteIncomePopUp(
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
private fun DeleteIncomePopUp(
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialogFinance(
        onDismissRequest = onDismissRequest,
        onConfirmation = onDelete,
        dialogTitle = "Delete",
        dialogText = "Do you want to delete this income?"
    )
}
