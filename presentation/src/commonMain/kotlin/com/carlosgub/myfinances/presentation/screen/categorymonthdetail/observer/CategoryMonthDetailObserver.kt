package com.carlosgub.myfinances.presentation.screen.categorymonthdetail.observer

import com.carlosgub.myfinances.domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import com.carlosgub.myfinances.domain.model.ExpenseScreenModel
import com.carlosgub.myfinances.domain.model.FinanceEnum
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetail.CategoryMonthDetailScreenSideEffect
import moe.tlaster.precompose.navigation.Navigator

fun categoryMonthDetailObserver(
    sideEffect: CategoryMonthDetailScreenSideEffect,
    navigator: Navigator,
    appNavigation: AppNavigation,
) {
    when (sideEffect) {
        is CategoryMonthDetailScreenSideEffect.NavigateToMonthDetail ->
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
    val financeEnum = getCategoryEnumFromName(name = expenseScreenModel.category).type
    if (financeEnum == FinanceEnum.EXPENSE) {
        appNavigation.navigateToEditExpense(
            navigator = navigator,
            id = expenseScreenModel.id,
        )
    } else {
        appNavigation.navigateToEditIncome(
            navigator = navigator,
            id = expenseScreenModel.id,
        )
    }
}
