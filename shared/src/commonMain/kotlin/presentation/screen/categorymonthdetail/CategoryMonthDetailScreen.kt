@file:OptIn(ExperimentalResourceApi::class)

package presentation.screen.categorymonthdetail

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import core.navigation.LocalNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import presentation.screen.categorymonthdetail.content.CategoryMonthDetailContent
import presentation.screen.categorymonthdetail.content.categoryMonthDetailObserver
import presentation.viewmodel.categorymonthdetail.CategoryMonthDetailViewModel
import utils.views.Toolbar

@Composable
fun CategoryMonthDetailScreen(
    monthKey: String,
    categoryName: String,
) {
    val navigator = LocalNavController.current
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
