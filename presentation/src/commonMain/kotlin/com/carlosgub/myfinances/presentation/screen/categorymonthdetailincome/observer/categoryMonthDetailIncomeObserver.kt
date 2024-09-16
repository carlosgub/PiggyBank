package com.carlosgub.myfinances.presentation.screen.categorymonthdetailincome.observer

import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailincome.CategoryMonthDetailIncomeScreenSideEffect
import moe.tlaster.precompose.navigation.Navigator

fun categoryMonthDetailIncomeObserver(
    sideEffect: CategoryMonthDetailIncomeScreenSideEffect,
    navigator: Navigator,
    appNavigation: AppNavigation,
) {
    when (sideEffect) {
        is CategoryMonthDetailIncomeScreenSideEffect.NavigateToMonthDetail ->
            navigateToEditScreen(
                navigator = navigator,
                id = sideEffect.incomeScreenModel.id,
                appNavigation = appNavigation,
            )
    }
}

private fun navigateToEditScreen(
    navigator: Navigator,
    id: Long,
    appNavigation: AppNavigation,
) {
    appNavigation.navigateToEditIncome(
        navigator = navigator,
        id = id,
    )
}
