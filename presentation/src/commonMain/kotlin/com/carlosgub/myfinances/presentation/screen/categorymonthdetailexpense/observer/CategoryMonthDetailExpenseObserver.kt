package com.carlosgub.myfinances.presentation.screen.categorymonthdetailexpense.observer

import com.carlosgub.myfinances.presentation.model.ExpenseScreenModel
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailexpense.CategoryMonthDetailExpenseScreenSideEffect
import moe.tlaster.precompose.navigation.Navigator

fun categoryMonthDetailExpenseObserver(
    sideEffect: CategoryMonthDetailExpenseScreenSideEffect,
    navigator: Navigator,
    appNavigation: AppNavigation,
) {
    when (sideEffect) {
        is CategoryMonthDetailExpenseScreenSideEffect.NavigateToMonthDetail ->
            navigateToEditScreen(
                navigator = navigator,
                expenseScreenModel = sideEffect.expenseScreenModel,
                appNavigation = appNavigation,
            )
    }
}

private fun navigateToEditScreen(
    navigator: Navigator,
    expenseScreenModel: ExpenseScreenModel,
    appNavigation: AppNavigation,
) {
    appNavigation.navigateToEditExpense(
        navigator = navigator,
        id = expenseScreenModel.id,
    )
}
