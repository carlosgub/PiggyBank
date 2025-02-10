package com.carlosgub.myfinances.presentation.viewmodel.home

sealed class HomeScreenSideEffect {
    data object NavigateToMonths : HomeScreenSideEffect()

    data class NavigateToMonthExpenseDetail(
        val categoryName: String,
    ) : HomeScreenSideEffect()

    data class NavigateToMonthIncomeDetail(
        val categoryName: String,
    ) : HomeScreenSideEffect()

    data object NavigateToAddExpense : HomeScreenSideEffect()

    data object NavigateToAddIncome : HomeScreenSideEffect()
}
