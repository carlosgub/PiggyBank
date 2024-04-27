package presentation.screen.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import core.navigation.LocalNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import domain.model.MenuItem
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import presentation.screen.home.content.HomeContent
import presentation.screen.home.content.homeObserver
import presentation.viewmodel.home.HomeViewModel
import utils.getCurrentMonthKey
import utils.views.Toolbar

@Composable
fun HomeScreen(
    monthKey: String
) {
    val navigator = LocalNavController.current
    val viewModel = koinViewModel(vmClass = HomeViewModel::class)
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    viewModel.setMonthKey(monthKey)
    val scope = CoroutineScope(Dispatchers.Main)
    Scaffold(
        topBar = {
            HomeToolbar(
                showLeftIcon = monthKey == getCurrentMonthKey(),
                onAddExpensePressed = {
                    viewModel.navigateToAddExpense()
                },
                onAddIncomePressed = {
                    viewModel.navigateToAddIncome()
                },
                onCalendarPressed = {
                    viewModel.navigateToMonths()
                },
                onBack = {
                    navigator.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        HomeContent(
            paddingValues = paddingValues,
            state = state,
            intents = viewModel
        )
    }
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            homeObserver(
                sideEffect = sideEffect,
                navigator = navigator,
                state = state,
            )
        }
    }
}

@Composable
private fun HomeToolbar(
    showLeftIcon: Boolean,
    onAddExpensePressed: () -> Unit,
    onAddIncomePressed: () -> Unit,
    onCalendarPressed: () -> Unit,
    onBack: () -> Unit
) {
    val leftIcon = if (showLeftIcon) Icons.Filled.CalendarMonth else null
    Toolbar(
        hasNavigationIcon = !showLeftIcon,
        navigation = onBack,
        title = "My Finances",
        dropDownIcon = Icons.Filled.Add,
        dropDownMenu = true,
        leftIcon = leftIcon,
        onLeftIconPressed = onCalendarPressed,
        dropDownItems = mutableListOf(
            MenuItem(
                name = "Add Expense",
                icon = Icons.Filled.MoneyOff,
                onItemClicked = onAddExpensePressed
            ),
            MenuItem(
                name = "Add Income",
                icon = Icons.Filled.AttachMoney,
                onItemClicked = onAddIncomePressed
            )
        )
    )
}
