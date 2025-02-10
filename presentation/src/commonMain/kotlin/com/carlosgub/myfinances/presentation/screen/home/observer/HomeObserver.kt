package com.carlosgub.myfinances.presentation.screen.home.observer

import androidx.navigation.NavHostController
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeScreenSideEffect
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeScreenState

fun homeObserver(
    sideEffect: HomeScreenSideEffect,
    navController: NavHostController,
    state: HomeScreenState,
    appNavigation: AppNavigation,
) {
    when (sideEffect) {
        HomeScreenSideEffect.NavigateToAddExpense ->
            appNavigation.navigateToAddExpense(
                navController = navController,
            )

        HomeScreenSideEffect.NavigateToAddIncome ->
            appNavigation.navigateToAddIncome(
                navController = navController,
            )

        is HomeScreenSideEffect.NavigateToMonthExpenseDetail -> appNavigation.navigateToMonthExpenseDetail(
            navController = navController,
            categoryName = sideEffect.categoryName,
            monthKey = state.monthKey,
        )

        is HomeScreenSideEffect.NavigateToMonthIncomeDetail -> appNavigation.navigateToMonthIncomeDetail(
            navController = navController,
            categoryName = sideEffect.categoryName,
            monthKey = state.monthKey,
        )

        HomeScreenSideEffect.NavigateToMonths -> appNavigation.navigateToMonths(
            navController = navController,
        )
    }
}
