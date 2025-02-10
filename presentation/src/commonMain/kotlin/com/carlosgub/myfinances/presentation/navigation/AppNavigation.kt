package com.carlosgub.myfinances.presentation.navigation

import androidx.navigation.NavHostController


interface AppNavigation {
    fun navigateToEditIncome(
        navController: NavHostController,
        id: Long,
    )

    fun navigateToHome(
        navController: NavHostController,
        monthKey: String,
    )

    fun navigateToEditExpense(
        navController: NavHostController,
        id: Long,
    )

    fun navigateToMonthExpenseDetail(
        navController: NavHostController,
        monthKey: String,
        categoryName: String,
    )

    fun navigateToMonthIncomeDetail(
        navController: NavHostController,
        monthKey: String,
        categoryName: String,
    )

    fun navigateToMonths(navController: NavHostController)

    fun navigateToAddExpense(navController: NavHostController)

    fun navigateToAddIncome(navController: NavHostController)
}
