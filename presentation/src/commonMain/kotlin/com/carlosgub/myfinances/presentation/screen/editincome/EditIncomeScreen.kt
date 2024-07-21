package com.carlosgub.myfinances.presentation.screen.editincome

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
import com.carlosgub.myfinances.presentation.screen.editincome.content.EditIncomeContent
import com.carlosgub.myfinances.presentation.screen.editincome.observer.editIncomeObserver
import com.carlosgub.myfinances.presentation.viewmodel.editincome.EditIncomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import myfinances.presentation.generated.resources.Res
import myfinances.presentation.generated.resources.edit_income_pop_up_message
import myfinances.presentation.generated.resources.edit_income_pop_up_title
import myfinances.presentation.generated.resources.edit_income_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun EditIncomeScreen(
    id: Long,
    modifier: Modifier = Modifier,
    viewModel: EditIncomeViewModel = koinInject(),
) {
    val navigator = LocalNavController.current
    val scope = CoroutineScope(Dispatchers.Main)
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getIncome(
            id = id,
        )
    }
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            editIncomeObserver(
                sideEffect = sideEffect,
                navigator = navigator,
            )
        }
    }
    Scaffold(
        topBar = {
            EditIncomeToolbar(
                onBack = { navigator.popBackStack() },
                onDelete = {
                    viewModel.delete()
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        EditIncomeContent(
            paddingValues = paddingValues,
            state = state,
            intents = viewModel,
        )
    }
}

@Composable
private fun EditIncomeToolbar(
    onBack: () -> Unit,
    onDelete: () -> Unit,
) {
    var popUpVisible by remember { mutableStateOf(false) }
    Toolbar(
        hasNavigationIcon = true,
        title = stringResource(Res.string.edit_income_title),
        navigation = onBack,
        leftIcon = Icons.Default.Delete,
        onLeftIconPressed = {
            popUpVisible = true
        },
    )
    if (popUpVisible) {
        DeleteIncomePopUp(
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
private fun DeleteIncomePopUp(
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        onConfirmation = onDelete,
        dialogTitle = stringResource(Res.string.edit_income_pop_up_title),
        dialogText = stringResource(Res.string.edit_income_pop_up_message),
    )
}
