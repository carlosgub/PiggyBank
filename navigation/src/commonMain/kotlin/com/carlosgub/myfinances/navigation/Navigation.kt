package com.carlosgub.myfinances.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Navigation(
    val route: String,
) {
    data object Home : Navigation("Home/{${NavArgs.MONTH_KEY.key}}") {
        fun createRoute(monthKey: String) = "Home/$monthKey"
    }

    data object CreateIncomeScreen : Navigation("CreateIncomeScreen")

    data object CreateExpenseScreen : Navigation("CreateExpenseScreen")

    data object EditExpenseScreen :
        Navigation("EditExpenseScreen?${NavArgs.ID.key}={${NavArgs.ID.key}}") {
        fun createRoute(id: Long) = "EditExpenseScreen?${NavArgs.ID.key}=$id"
        fun getArguments() = listOf(
            navArgument(NavArgs.ID.key) {
                type = NavType.LongType
                defaultValue = 1L
                nullable = false
            },
        )
    }

    data object EditIncomeScreen :
        Navigation("EditIncomeScreen?${NavArgs.ID.key}={${NavArgs.ID.key}}") {
        fun createRoute(id: Long) = "EditIncomeScreen?${NavArgs.ID.key}=$id"
        fun getArguments() = listOf(
            navArgument(NavArgs.ID.key) {
                type = NavType.LongType
                defaultValue = 1L
                nullable = false
            },
        )
    }

    data object MonthsScreen : Navigation("MonthsScreen")

    data object CategoryMonthDetailExpenseScreen :
        Navigation("CategoryMonthDetailExpenseScreen/{${NavArgs.MONTH_KEY.key}}/{${NavArgs.CATEGORY_NAME.key}}") {
        fun createRoute(
            monthKey: String,
            categoryName: String,
        ) = "CategoryMonthDetailExpenseScreen/$monthKey/$categoryName"
    }

    data object CategoryMonthDetailIncomeScreen :
        Navigation("CategoryMonthDetailIncomeScreen/{${NavArgs.MONTH_KEY.key}}/{${NavArgs.CATEGORY_NAME.key}}") {
        fun createRoute(
            monthKey: String,
            categoryName: String,
        ) = "CategoryMonthDetailIncomeScreen/$monthKey/$categoryName"
    }
}

enum class NavArgs(
    val key: String,
) {
    ID("id"),
    MONTH_KEY("monthKey"),
    CATEGORY_NAME("categoryName"),
}
