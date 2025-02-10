package com.carlosgub.myfinances.navigation.impl

import androidx.navigation.NavHostController
import com.carlosgub.myfinances.navigation.Navigation
import com.carlosgub.myfinances.presentation.navigation.AppNavigation

class AppNavigationImpl : AppNavigation {
    override fun navigateToEditIncome(
        navController: NavHostController,
        id: Long,
    ) {
        navController.navigate(
            Navigation.EditIncomeScreen.createRoute(
                id = id,
            ),
        )
    }

    override fun navigateToHome(
        navController: NavHostController,
        monthKey: String,
    ) {
        navController.navigate(
            Navigation.Home.createRoute(
                monthKey = monthKey,
            ),
        )
    }

    override fun navigateToEditExpense(
        navController: NavHostController,
        id: Long,
    ) {
        navController.navigate(
            Navigation.EditExpenseScreen.createRoute(
                id = id,
            ),
        )
    }

    override fun navigateToMonthExpenseDetail(
        navController: NavHostController,
        monthKey: String,
        categoryName: String,
    ) {
        navController.navigate(
            Navigation.CategoryMonthDetailExpenseScreen.createRoute(
                monthKey = monthKey,
                categoryName = categoryName,
            ),
        )
    }

    override fun navigateToMonthIncomeDetail(
        navController: NavHostController,
        monthKey: String,
        categoryName: String,
    ) {
        navController.navigate(
            Navigation.CategoryMonthDetailIncomeScreen.createRoute(
                monthKey = monthKey,
                categoryName = categoryName,
            ),
        )
    }

    override fun navigateToMonths(navController: NavHostController) {
        navController.navigate(Navigation.MonthsScreen.route)
    }

    override fun navigateToAddExpense(navController: NavHostController) {
        navController.navigate(Navigation.CreateExpenseScreen.route)
    }

    override fun navigateToAddIncome(navController: NavHostController) {
        navController.navigate(Navigation.CreateIncomeScreen.route)
    }
}
