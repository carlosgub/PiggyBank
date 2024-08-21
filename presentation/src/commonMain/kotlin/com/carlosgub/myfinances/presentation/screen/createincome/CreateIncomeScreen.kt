package com.carlosgub.myfinances.presentation.screen.createincome

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.carlosgub.myfinances.components.toolbar.Toolbar
import com.carlosgub.myfinances.core.navigation.LocalNavController
import com.carlosgub.myfinances.presentation.screen.createincome.content.CreateIncomeContent
import com.carlosgub.myfinances.presentation.screen.createincome.observer.createIncomeObserver
import com.carlosgub.myfinances.presentation.viewmodel.createincome.CreateIncomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import piggybank.presentation.generated.resources.Res
import piggybank.presentation.generated.resources.create_income_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun CreateIncomeScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateIncomeViewModel = koinInject(),
) {
    val navigator = LocalNavController.current
    val scope = CoroutineScope(Dispatchers.Main)
    val createScreenState by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            createIncomeObserver(
                sideEffect = sideEffect,
                navigator = navigator,
            )
        }
    }
    Scaffold(
        topBar = {
            CreateIncomeToolbar(
                onBack = {
                    navigator.popBackStack()
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        CreateIncomeContent(
            state = createScreenState,
            intents = viewModel,
            modifier = Modifier
                .padding(paddingValues),
        )
    }
}

@Composable
private fun CreateIncomeToolbar(onBack: () -> Unit) {
    Toolbar(
        hasNavigationIcon = true,
        title = stringResource(Res.string.create_income_title),
        navigation = onBack,
    )
}
