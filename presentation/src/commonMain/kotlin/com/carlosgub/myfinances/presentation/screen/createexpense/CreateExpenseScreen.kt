package com.carlosgub.myfinances.presentation.screen.createexpense

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carlosgub.myfinances.components.toolbar.Toolbar
import com.carlosgub.myfinances.core.navigation.LocalNavController
import com.carlosgub.myfinances.presentation.screen.createexpense.content.CreateExpenseContent
import com.carlosgub.myfinances.presentation.screen.createexpense.observer.createExpenseObserver
import com.carlosgub.myfinances.presentation.viewmodel.createexpense.CreateExpenseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import piggybank.presentation.generated.resources.Res
import piggybank.presentation.generated.resources.create_expense_title

@Composable
fun CreateExpenseScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateExpenseViewModel = koinInject(),
) {
    val navController = LocalNavController.current
    val scope = CoroutineScope(Dispatchers.Main)
    val createScreenState by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            createExpenseObserver(
                sideEffect = sideEffect,
                navController = navController,
            )
        }
    }
    Scaffold(
        topBar = {
            CreateExpenseToolbar(
                onBack = {
                    navController.popBackStack()
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        CreateExpenseContent(
            state = createScreenState,
            intents = viewModel,
            modifier = Modifier
                .padding(paddingValues),
        )
    }
}

@Composable
private fun CreateExpenseToolbar(onBack: () -> Unit) {
    Toolbar(
        hasNavigationIcon = true,
        title = stringResource(Res.string.create_expense_title),
        navigation = onBack,
    )
}
