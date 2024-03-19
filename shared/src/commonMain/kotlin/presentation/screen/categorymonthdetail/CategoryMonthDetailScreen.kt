package presentation.screen.categorymonthdetail

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import core.navigation.LocalNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.CategoryMonthDetailArgs
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import presentation.screen.categorymonthdetail.content.CategoryMonthDetailContent
import presentation.screen.categorymonthdetail.content.categoryMonthDetailObserver
import presentation.viewmodel.monthDetail.CategoryMonthDetailViewModel
import utils.views.Toolbar

@Composable
fun CategoryMonthDetailScreen(
    args: CategoryMonthDetailArgs
) {
    val navigator = LocalNavController.current
    val viewModel = koinViewModel(vmClass = CategoryMonthDetailViewModel::class)
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    viewModel.setInitialConfiguration(args)
    val scope = CoroutineScope(Dispatchers.Main)
    Scaffold(
        topBar = {
            ExpenseMonthDetailToolbar(
                category = state.category.categoryName,
                onBack = {
                    navigator.goBackWith(state.updateBackScreen)
                }
            )
        }
    ) { paddingValues ->
        CategoryMonthDetailContent(
            paddingValues = paddingValues,
            state = state,
            expenseClicked = { expenseScreenModel ->
                viewModel.navigateToEditExpense(expenseScreenModel)
            }
        )
    }
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            categoryMonthDetailObserver(
                sideEffect = sideEffect,
                navigator = navigator,
                intents = viewModel,
                coroutine = this
            )
        }
    }
}

@Composable
private fun ExpenseMonthDetailToolbar(
    category: String,
    onBack: () -> Unit
) {
    Toolbar(
        backgroundColor = Color.White,
        title = category,
        hasNavigationIcon = true,
        navigation = onBack,
        contentColor = Color.Black
    )
}