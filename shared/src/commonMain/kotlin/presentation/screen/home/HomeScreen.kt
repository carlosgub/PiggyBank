@file:OptIn(ExperimentalResourceApi::class)

package presentation.screen.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import core.navigation.LocalNavController
import domain.model.MenuItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import myapplication.shared.generated.resources.Res
import myapplication.shared.generated.resources.home_add_expense
import myapplication.shared.generated.resources.home_add_income
import myapplication.shared.generated.resources.home_app_name
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import presentation.screen.home.content.HomeContent
import presentation.screen.home.content.homeObserver
import presentation.viewmodel.home.HomeViewModel
import utils.getCurrentMonthKey
import utils.views.Toolbar

@Composable
fun HomeScreen(
    monthKey: String,
    modifier: Modifier = Modifier
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
        },
        modifier = modifier
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
                state = state
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
        title = stringResource(Res.string.home_app_name),
        dropDownIcon = Icons.Filled.Add,
        dropDownMenu = true,
        leftIcon = leftIcon,
        onLeftIconPressed = onCalendarPressed,
        dropDownItems =
        persistentListOf(
            MenuItem(
                name = stringResource(Res.string.home_add_expense),
                icon = Icons.Filled.MoneyOff,
                onItemClicked = onAddExpensePressed
            ),
            MenuItem(
                name = stringResource(Res.string.home_add_income),
                icon = Icons.Filled.AttachMoney,
                onItemClicked = onAddIncomePressed
            )
        )
    )
}
