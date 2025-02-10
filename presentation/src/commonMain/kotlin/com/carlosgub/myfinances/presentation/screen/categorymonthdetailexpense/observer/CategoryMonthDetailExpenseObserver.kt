package com.carlosgub.myfinances.presentation.screen.categorymonthdetailexpense.observer

import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.carlosgub.myfinances.presentation.model.ExpenseScreenModel
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailexpense.CategoryMonthDetailExpenseScreenSideEffect

fun categoryMonthDetailExpenseObserver(
    sideEffect: CategoryMonthDetailExpenseScreenSideEffect,
    navController: NavHostController,
    appNavigation: AppNavigation,
) {
    when (sideEffect) {
        is CategoryMonthDetailExpenseScreenSideEffect.NavigateToMonthDetail ->
            navigateToEditScreen(
                navController = navController,
                expenseScreenModel = sideEffect.expenseScreenModel,
                appNavigation = appNavigation,
            )
    }
}

private fun navigateToEditScreen(
    navController: NavHostController,
    expenseScreenModel: ExpenseScreenModel,
    appNavigation: AppNavigation,
) {
    appNavigation.navigateToEditExpense(
        navController = navController,
        id = expenseScreenModel.id,
    )
}
