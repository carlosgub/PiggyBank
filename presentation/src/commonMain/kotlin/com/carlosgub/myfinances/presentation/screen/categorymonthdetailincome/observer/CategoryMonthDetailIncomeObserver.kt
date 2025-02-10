package com.carlosgub.myfinances.presentation.screen.categorymonthdetailincome.observer

import androidx.navigation.NavHostController
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailincome.CategoryMonthDetailIncomeScreenSideEffect

fun categoryMonthDetailIncomeObserver(
    sideEffect: CategoryMonthDetailIncomeScreenSideEffect,
    navController: NavHostController,
    appNavigation: AppNavigation,
) {
    when (sideEffect) {
        is CategoryMonthDetailIncomeScreenSideEffect.NavigateToMonthDetail ->
            navigateToEditScreen(
                navController = navController,
                id = sideEffect.incomeScreenModel.id,
                appNavigation = appNavigation,
            )
    }
}

private fun navigateToEditScreen(
    navController: NavHostController,
    id: Long,
    appNavigation: AppNavigation,
) {
    appNavigation.navigateToEditIncome(
        navController = navController,
        id = id,
    )
}
