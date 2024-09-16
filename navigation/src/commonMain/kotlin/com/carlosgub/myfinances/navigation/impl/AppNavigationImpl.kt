package com.carlosgub.myfinances.navigation.impl

import com.carlosgub.myfinances.navigation.Navigation
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import moe.tlaster.precompose.navigation.Navigator

class AppNavigationImpl : AppNavigation {
    override fun navigateToEditIncome(
        navigator: Navigator,
        id: Long,
    ) {
        navigator.navigate(
            Navigation.EditIncomeScreen.createRoute(
                id = id,
            ),
        )
    }

    override fun navigateToHome(
        navigator: Navigator,
        monthKey: String,
    ) {
        navigator.navigate(
            Navigation.Home.createRoute(
                monthKey = monthKey,
            ),
        )
    }

    override fun navigateToEditExpense(
        navigator: Navigator,
        id: Long,
    ) {
        navigator.navigate(
            Navigation.EditExpenseScreen.createRoute(
                id = id,
            ),
        )
    }

    override fun navigateToMonthExpenseDetail(
        navigator: Navigator,
        monthKey: String,
        categoryName: String,
    ) {
        navigator.navigate(
            Navigation.CategoryMonthDetailExpenseScreen.createRoute(
                monthKey = monthKey,
                categoryName = categoryName,
            ),
        )
    }

    override fun navigateToMonthIncomeDetail(
        navigator: Navigator,
        monthKey: String,
        categoryName: String,
    ) {
        navigator.navigate(
            Navigation.CategoryMonthDetailIncomeScreen.createRoute(
                monthKey = monthKey,
                categoryName = categoryName,
            ),
        )
    }

    override fun navigateToMonths(navigator: Navigator) {
        navigator.navigate(Navigation.MonthsScreen.route)
    }

    override fun navigateToAddExpense(navigator: Navigator) {
        navigator.navigate(Navigation.CreateExpenseScreen.route)
    }

    override fun navigateToAddIncome(navigator: Navigator) {
        navigator.navigate(Navigation.CreateIncomeScreen.route)
    }
}
