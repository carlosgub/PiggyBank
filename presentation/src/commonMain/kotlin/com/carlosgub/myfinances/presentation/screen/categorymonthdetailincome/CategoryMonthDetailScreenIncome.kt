package com.carlosgub.myfinances.presentation.screen.categorymonthdetailincome

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.carlosgub.myfinances.components.toolbar.Toolbar
import com.carlosgub.myfinances.core.navigation.LocalNavController
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.screen.categorymonthdetailincome.content.CategoryMonthDetailIncomeContent
import com.carlosgub.myfinances.presentation.screen.categorymonthdetailincome.observer.categoryMonthDetailIncomeObserver
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailincome.CategoryMonthDetailIncomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun CategoryMonthDetailScreenIncome(
    monthKey: String,
    categoryName: String,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavController.current
    val appNavigation: AppNavigation = koinInject()
    val viewModel = koinViewModel(vmClass = CategoryMonthDetailIncomeViewModel::class)
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    viewModel.setInitialConfiguration(monthKey = monthKey, category = categoryName)
    val scope = CoroutineScope(Dispatchers.Main)
    Scaffold(
        topBar = {
            CategoryMonthDetailIncomeToolbar(
                category = stringResource(state.category.categoryName),
                onBack = {
                    navigator.popBackStack()
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        CategoryMonthDetailIncomeContent(
            paddingValues = paddingValues,
            state = state,
            incomeClicked = { incomeScreenModel ->
                viewModel.navigateToEditIncome(incomeScreenModel)
            },
        )
    }
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            categoryMonthDetailIncomeObserver(
                sideEffect = sideEffect,
                navigator = navigator,
                appNavigation = appNavigation,
            )
        }
    }
}

@Composable
private fun CategoryMonthDetailIncomeToolbar(
    category: String,
    onBack: () -> Unit,
) {
    Toolbar(
        backgroundColor = Color.White,
        title = category,
        hasNavigationIcon = true,
        navigation = onBack,
        contentColor = Color.Black,
    )
}
