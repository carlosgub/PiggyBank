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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import piggybank.presentation.generated.resources.Res
import piggybank.presentation.generated.resources.home_add_expense
import piggybank.presentation.generated.resources.home_add_income
import piggybank.presentation.generated.resources.home_app_name
import piggybank.presentation.generated.resources.home_drop_down_menu_content_description
import piggybank.presentation.generated.resources.home_left_icon_content_description

@Composable
fun HomeScreen(
    monthKey: String,
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current
    val appNavigation: AppNavigation = koinInject()
    val viewModel = koinViewModel<HomeViewModel>()
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
                    navController.popBackStack()
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
                navController = navController,
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
    val leftIconContentDescription = if (showLeftIcon) {
        stringResource(Res.string.home_left_icon_content_description)
    } else {
        null
    }
    Toolbar(
        hasNavigationIcon = !showLeftIcon,
        navigation = onBack,
        title = stringResource(Res.string.home_app_name),
        dropDownIcon = Icons.Filled.Add,
        dropDownMenu = true,
        dropDownMenuContentDescription = stringResource(Res.string.home_drop_down_menu_content_description),
        leftIcon = leftIcon,
        leftIconContentDescription = leftIconContentDescription,
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
