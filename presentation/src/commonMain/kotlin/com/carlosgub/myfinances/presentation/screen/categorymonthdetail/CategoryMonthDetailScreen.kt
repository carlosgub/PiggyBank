package com.carlosgub.myfinances.presentation.screen.categorymonthdetail

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.carlosgub.myfinances.components.toolbar.Toolbar
import com.carlosgub.myfinances.core.navigation.LocalNavController
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.screen.categorymonthdetail.content.CategoryMonthDetailContent
import com.carlosgub.myfinances.presentation.screen.categorymonthdetail.observer.categoryMonthDetailObserver
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetail.CategoryMonthDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun CategoryMonthDetailScreen(
    monthKey: String,
    categoryName: String,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavController.current
    val appNavigation: AppNavigation = koinInject()
    val viewModel = koinViewModel(vmClass = CategoryMonthDetailViewModel::class)
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    viewModel.setInitialConfiguration(monthKey = monthKey, category = categoryName)
    val scope = CoroutineScope(Dispatchers.Main)
    Scaffold(
        topBar = {
            ExpenseMonthDetailToolbar(
                category = stringResource(state.category.categoryName),
                onBack = {
                    navigator.popBackStack()
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        CategoryMonthDetailContent(
            paddingValues = paddingValues,
            state = state,
            expenseClicked = { expenseScreenModel ->
                viewModel.navigateToEditExpense(expenseScreenModel)
            },
        )
    }
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            categoryMonthDetailObserver(
                sideEffect = sideEffect,
                navigator = navigator,
                appNavigation = appNavigation,
            )
        }
    }
}

@Composable
private fun ExpenseMonthDetailToolbar(
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
