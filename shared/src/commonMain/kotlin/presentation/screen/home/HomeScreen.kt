package presentation.screen.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.HomeArgs
import model.MenuItem
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import presentation.screen.home.content.HomeContent
import presentation.screen.home.content.homeObserver
import presentation.viewmodel.home.HomeViewModel
import utils.views.Toolbar

@Composable
fun HomeScreen(
    navigator: Navigator,
    args: HomeArgs
) {
    val viewModel = koinViewModel(vmClass = HomeViewModel::class)
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    viewModel.setMonthKey(args.monthKey)
    val scope = CoroutineScope(Dispatchers.Main)
    Scaffold(
        topBar = {
            HomeToolbar(
                isHome = args.isHome,
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
                    navigator.goBack()
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
                intents = viewModel,
                state = state,
                coroutine = this
            )
        }
    }
}

@Composable
private fun HomeToolbar(
    isHome: Boolean,
    onAddExpensePressed: () -> Unit,
    onAddIncomePressed: () -> Unit,
    onCalendarPressed: () -> Unit,
    onBack: () -> Unit
) {
    val leftIcon = if (isHome) Icons.Filled.CalendarMonth else null
    Toolbar(
        hasNavigationIcon = !isHome,
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
