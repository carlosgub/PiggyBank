package com.carlosgub.myfinances.presentation.screen.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.carlosgub.myfinances.components.toolbar.Toolbar
import com.carlosgub.myfinances.components.toolbar.parameter.MenuItem
import com.carlosgub.myfinances.core.navigation.LocalNavController
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.screen.home.content.HomeContent
import com.carlosgub.myfinances.presentation.screen.home.observer.homeObserver
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import myfinances.presentation.generated.resources.Res
import myfinances.presentation.generated.resources.home_add_expense
import myfinances.presentation.generated.resources.home_add_income
import myfinances.presentation.generated.resources.home_app_name
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    monthKey: String,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavController.current
    val appNavigation: AppNavigation = koinInject()
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
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        HomeContent(
            paddingValues = paddingValues,
            state = state,
            intents = viewModel,
        )
    }
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            homeObserver(
                sideEffect = sideEffect,
                navigator = navigator,
                state = state,
                appNavigation = appNavigation,
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
    onBack: () -> Unit,
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
        dropDownItems = persistentListOf(
            MenuItem(
                name = stringResource(Res.string.home_add_expense),
                icon = Icons.Filled.MoneyOff,
                onItemClicked = onAddExpensePressed,
            ),
            MenuItem(
                name = stringResource(Res.string.home_add_income),
                icon = Icons.Filled.AttachMoney,
                onItemClicked = onAddIncomePressed,
            ),
        ),
    )
}
