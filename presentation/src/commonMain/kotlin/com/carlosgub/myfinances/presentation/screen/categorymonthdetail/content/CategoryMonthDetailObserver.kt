package com.carlosgub.myfinances.presentation.screen.categorymonthdetail.content

import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetail.CategoryMonthDetailScreenSideEffect
import domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import domain.model.ExpenseScreenModel
import domain.model.FinanceEnum
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
