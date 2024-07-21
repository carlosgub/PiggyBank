package com.carlosgub.myfinances.presentation.screen.month

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.carlosgub.myfinances.components.toolbar.Toolbar
import com.carlosgub.myfinances.core.navigation.LocalNavController
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.screen.month.content.MonthsContent
import com.carlosgub.myfinances.presentation.screen.month.observer.monthsObserver
import com.carlosgub.myfinances.presentation.viewmodel.months.MonthsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import myfinances.presentation.generated.resources.Res
import myfinances.presentation.generated.resources.months_toolbar_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun MonthsScreen(modifier: Modifier = Modifier) {
    val navigator = LocalNavController.current
    val appNavigation: AppNavigation = koinInject()
    val viewModel = koinViewModel(vmClass = MonthsViewModel::class)
    val monthsScreenState by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val scope = CoroutineScope(Dispatchers.Main)
    Scaffold(
        topBar = {
            MonthsToolbar(
                onBack = {
                    navigator.popBackStack()
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        MonthsContent(
            monthsScreenState = monthsScreenState,
            paddingValues = paddingValues,
            onMonthClicked = { monthKey ->
                viewModel.navigateToMonthDetail(
                    monthKey = monthKey,
                )
            },
        )
    }
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            monthsObserver(
                sideEffect = sideEffect,
                navigator = navigator,
                appNavigation = appNavigation,
            )
        }
    }
}

@Composable
private fun MonthsToolbar(onBack: () -> Unit) {
    Toolbar(
        backgroundColor = Color.White,
        hasNavigationIcon = true,
        navigation = onBack,
        contentColor = Color.Black,
        title = stringResource(Res.string.months_toolbar_title),
    )
}
